/*
 * Copyright (c) 2021. Noah Levitt
 * All rights reserved.
 */

package playertypes

import enums.Action

open class BasicStrategyPlayer(name: String) : Player(name) {
    override fun nextAction(dealerTopCardValue: Int): Action {
        println("$this using basic strategy...")
        return when {
            splittable -> splitStrategy(dealerTopCardValue)
            soft -> softStrategy(dealerTopCardValue)
            else -> hardStrategy(dealerTopCardValue)
        }
    }

    private fun hardStrategy(dealerValue: Int): Action {
        return when {
            score == 9 && dealerValue in 3..6 -> if (doubleable) Action.DOUBLE else Action.HIT
            score == 10 && dealerValue < 10 -> if (doubleable) Action.DOUBLE else Action.HIT
            score == 11 && dealerValue < 11 -> if (doubleable) Action.DOUBLE else Action.HIT

            score == 9 && dealerValue in 4..6 -> Action.STAND
            score > 12 && dealerValue < 7 -> Action.STAND
            score >= 17 -> Action.STAND

            else -> Action.HIT
        }
    }

    private fun softStrategy(dealerValue: Int): Action {
        return when {
            score in 13..14 && dealerValue in 5..6 -> if (doubleable) Action.DOUBLE else Action.HIT
            score in 15..16 && dealerValue in 4..6 -> if (doubleable) Action.DOUBLE else Action.HIT
            score == 17 && dealerValue in 3..6 -> if (doubleable) Action.DOUBLE else Action.HIT

            score == 18 && dealerValue in 3..6 -> if (doubleable) Action.DOUBLE else Action.STAND

            score == 18 && dealerValue < 9 -> Action.STAND
            score >= 19 -> Action.STAND

            else -> Action.HIT
        }
    }

    private fun splitStrategy(dealerValue: Int): Action {
        val pairValue = firstTwoCards[0].rank.rankValue
        return when {
            pairValue == 5 || pairValue == 10 -> hardStrategy(dealerValue)

            pairValue in setOf(2, 3, 4, 6, 7) && dealerValue >= 8 -> Action.HIT
            (pairValue == 4 || pairValue == 6) && dealerValue >= 7 -> Action.HIT
            pairValue == 4 && dealerValue < 5 -> Action.HIT

            pairValue == 9 && dealerValue in setOf(7, 10, 11) -> Action.STAND

            else -> Action.SPLIT
        }
    }
}