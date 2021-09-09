/*
 * Copyright (c) 2021. Noah Levitt
 * All rights reserved.
 */

package playertypes

import HandList
import cardutils.*
import enums.Action

abstract class Player(val name: String) {
    private val hands: HandList = mutableListOf(mutableListOf())
    private var active: Int = 0

    private val scores: List<Int>
        get() = hands.map { hand -> hand.sumOf { it.value } }
    protected val soft: Boolean
        get() = hands[active].any { it.value == 11 }

    protected val splittable: Boolean
        get() = hands[active].size == 2 && hands[active][0] == hands[active][1]
    protected val doubleable: Boolean
        get() = hands[active].size == 2 && score in 9..11
    protected val availableActions: Set<Action>
        get() {
            val actions: MutableSet<Action> = mutableSetOf(Action.HIT, Action.STAND)

            if (splittable) actions += Action.SPLIT
            if (doubleable) actions += Action.DOUBLE

            return actions
        }

    val firstTwoCards: List<Card>
        get() = hands[active].subList(0, 2)
    val topCard: Card
        get() = firstTwoCards[0]
    val score: Int
        get() = scores[active]
    val busted: Boolean
        get() = score > 21
    val blackjack: Boolean
        get() = score == 21

    abstract fun nextAction(dealerTopCardValue: Int): Action

    override fun toString(): String {
        val softString = if (soft) "soft " else ""
        val scoreString = if (blackjack) "Blackjack" else if (busted) "busted" else "$softString${score}"

        return "$name (score: $scoreString)"
    }

    open fun reset() {
        hands.clear()
        hands.add(mutableListOf())

        active = 0
    }

    open fun takeFrom(deck: Deck, next: Boolean = false): Card {
        val hand = if (next) hands[active + 1] else hands[active]

        val card = deck.deal()
        hand += card
        EnhancedStrategyPlayer.adjust(card.rank.rankValue)

        if (hand.sumOf { it.value } > 21) hand.find { it.value == 11 }?.value = 1
        return card
    }

    fun split(deck: Deck) {
        hands.add(active + 1, mutableListOf())
        hands[active + 1] += hands[active].removeLast()

        (hands[active] + hands[active + 1]).forEach { it.value = it.rank.rankValue }

        takeFrom(deck)
        takeFrom(deck, next = true)
    }

    fun switchHands(): Boolean {
        if (active < hands.size - 1) {
            active++
            return true
        }
        return false
    }

    fun beats(dealer: Dealer): Boolean = when {
        dealer.blackjack -> false
        dealer.busted -> scores.any { it <= 21 }
        else -> scores.any { it in dealer.score..21 }
    }
}