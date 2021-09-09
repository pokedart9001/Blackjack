/*
 * Copyright (c) 2021. Noah Levitt
 * All rights reserved.
 */

package playertypes

import enums.Action

class HumanPlayer(name: String) : Player(name,) {
    override fun nextAction(dealerTopCardValue: Int): Action {
        val actionString = availableActions.joinToString(", ") { it.actionName }

        var action: Action?
        do {
            print("$this, choose an action ($actionString): ")
            action = when (readLine()?.lowercase()?.get(0)) {
                'h' -> Action.HIT
                's' -> Action.STAND
                'p' -> Action.SPLIT
                'd' -> Action.DOUBLE
                else -> null
            }
        } while (action !is Action || action !in availableActions)

        return action
    }
}