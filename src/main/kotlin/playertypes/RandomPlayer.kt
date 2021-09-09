/*
 * Copyright (c) 2021. Noah Levitt
 * All rights reserved.
 */

package playertypes

import enums.Action

class RandomPlayer(name: String) : Player(name,) {
    override fun nextAction(dealerTopCardValue: Int): Action {
        println("$this choosing a random move...")
        return availableActions.random()
    }
}