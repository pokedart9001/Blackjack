/*
 * Copyright (c) 2021. Noah Levitt
 * All rights reserved.
 */

package playertypes

import enums.Action

class Dealer : Player("Dealer") {
    private var revealed: Boolean = false
    private val shouldHit: Boolean
        get() = score < 17

    override fun nextAction(dealerTopCardValue: Int): Action {
        println("$this playing...")
        return if (shouldHit) Action.HIT else Action.STAND
    }

    override fun toString(): String {
        val hiddenScoreString = "up card: $topCard, score: ???"
        return super.toString()
            .replace(Regex("score: ((?:soft )?\\d+)"), if (!revealed) hiddenScoreString else "score: $1")
    }

    override fun reset() {
        super.reset()
        revealed = false
    }

    fun reveal() {
        revealed = true
    }
}