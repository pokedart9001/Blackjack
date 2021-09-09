/*
 * Copyright (c) 2021. Noah Levitt
 * All rights reserved.
 */

import cardutils.Card
import java.util.PriorityQueue

const val NUM_SUITS = 4
const val NUM_DECKS = 4

typealias HandList = MutableList<MutableList<Card>>
typealias ProbabilityQueue<T> = PriorityQueue<Pair<T, Double>>