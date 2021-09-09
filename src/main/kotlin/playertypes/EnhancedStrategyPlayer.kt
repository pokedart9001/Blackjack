/*
 * Copyright (c) 2021. Noah Levitt
 * All rights reserved.
 */

package playertypes

import NUM_DECKS
import NUM_SUITS
import ProbabilityQueue
import enums.Action
import enums.Rank
import java.util.PriorityQueue
import kotlin.math.exp

class EnhancedStrategyPlayer(name: String) : BasicStrategyPlayer(name) {
    companion object {
        private val available: MutableMap<Int, Int> = mutableMapOf()

        private val numAvailableRanks: Int
            get() = available.values.sum()

        private val probabilities: ProbabilityQueue<Int>
            get() {
                val probQueue: ProbabilityQueue<Int> = PriorityQueue(compareByDescending { it.second })
                for ((key, value) in available.entries)
                    probQueue += key to exp(value.toDouble()) / available.values.sumOf { exp(it.toDouble()) }

                return probQueue
            }

        private val mostLikely: Pair<Int, Double>
            get() = probabilities.peek()

        private fun fill() {
            for (rank in Rank.values())
                available[rank.rankValue] = (available[rank.rankValue] ?: 0) + NUM_SUITS * NUM_DECKS
        }

        fun adjust(rankValue: Int) {
            available[rankValue] = available[rankValue]!! - 1
            if (numAvailableRanks == 0) fill()
        }
    }

    init {
        fill()
    }

    override fun nextAction(dealerTopCardValue: Int): Action {
        val (rankValue, probability) = mostLikely
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