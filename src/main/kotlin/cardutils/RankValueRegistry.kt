/*
 * Copyright (c) 2021. Noah Levitt
 * All rights reserved.
 */

package cardutils

import NUM_DECKS
import NUM_SUITS
import ProbabilityQueue
import enums.Rank
import java.util.*
import kotlin.math.exp

object RankValueRegistry {
    private val available: MutableMap<Int, Int> = mutableMapOf()

    private val numAvailableRanks: Int
        get() = available.values.sum()

    private val probabilities: ProbabilityQueue<Int>
        get() {
            val probQueue: ProbabilityQueue<Int> = PriorityQueue(compareByDescending { it.second })
            for ((key, value) in available.entries)
                probQueue += key to softmax(value, available.values)

            return probQueue
        }

    val mostLikely: Pair<Int, Double>
        get() = probabilities.peek()

    private fun softmax(x: Int, xs: MutableCollection<Int>) = exp(x.toDouble()) / xs.sumOf { exp(it.toDouble()) }

    fun fill() {
        for (rank in Rank.values())
            available[rank.rankValue] = (available[rank.rankValue] ?: 0) + NUM_SUITS * NUM_DECKS
    }

    fun adjust(rankValue: Int) {
        available[rankValue] = available[rankValue]!! - 1
        if (numAvailableRanks == 0) fill()
    }
}