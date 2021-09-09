/*
 * Copyright (c) 2021. Noah Levitt
 * All rights reserved.
 */

package cardutils

import enums.Rank
import enums.Suit

data class Card(val suit: Suit, val rank: Rank) {
    var value = rank.rankValue
    val name: String
        get() = rank.rankName

    override fun equals(other: Any?): Boolean = other is Card && rank.rankValue == other.rank.rankValue
    override fun hashCode(): Int = 31 * 31 * suit.hashCode() + rank.hashCode() + value

    override fun toString(): String = "${rank.rankName} of ${suit.suitName}"
}