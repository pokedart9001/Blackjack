/*
 * Copyright (c) 2021. Noah Levitt
 * All rights reserved.
 */

package playertypes

import cardutils.RankValueRegistry
import enums.Action

class EnhancedStrategyPlayer(name: String) : BasicStrategyPlayer(name) {
    override fun nextAction(dealerTopCardValue: Int): Action {
        val (rankValue, probability) = RankValueRegistry.mostLikely
        return when {
            probability > 0.65 -> {
                println("$this using probability...")
                probabilityStrategy(rankValue)
            }
            else -> super.nextAction(dealerTopCardValue)
        }
    }

    private fun probabilityStrategy(mostLikelyRankValue: Int): Action {
        return when {
            mostLikelyRankValue == 11 -> Action.HIT

            score + mostLikelyRankValue <= 21 -> if (doubleable) Action.DOUBLE else Action.HIT
            else -> if (splittable && score <= 17) Action.SPLIT else Action.STAND
        }
    }
}