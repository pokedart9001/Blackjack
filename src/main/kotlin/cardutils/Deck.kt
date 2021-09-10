/*
 * Copyright (c) 2021. Noah Levitt
 * All rights reserved.
 */

package cardutils

import NUM_DECKS
import enums.Rank
import enums.Suit
import playertypes.Player
import java.util.*

class Deck {
    private var cards: Stack<Card> = Stack()
    private val empty: Boolean
        get() = cards.isEmpty()

    init {
        reset()
    }

    private fun reset() {
        fill()
        shuffle()
    }

    private fun fill() {
        repeat(NUM_DECKS) {
            for (suit in Suit.values()) for (rank in Rank.values()) cards += Card(suit, rank)
        }
    }

    private fun shuffle() = Collections.shuffle(cards)

    infix fun spreadTo(players: List<Player>) {
        for (player in players) {
            player.takeFrom(this)
            player.takeFrom(this)
        }
    }

    fun deal(): Card {
        val card: Card = cards.pop()
        if (empty) reset()

        RankValueRegistry.adjust(card.rank.rankValue)
        return card
    }
}