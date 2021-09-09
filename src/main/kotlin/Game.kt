/*
 * Copyright (c) 2021. Noah Levitt
 * All rights reserved.
 */

import cardutils.*
import enums.Action
import playertypes.*

fun printlnln(arg: Any?) {
    println(arg)
    println()
}

fun playerTurn(player: Player, deck: Deck, dealerTopCardValue: Int = 0) {
    if (player.blackjack) {
        printlnln("${player.name} got a Natural Blackjack!")
        if (player.switchHands()) playerTurn(player, deck, dealerTopCardValue) else return
    }

    println("Starting cards: ${player.firstTwoCards.map { it.name }}")

    var continueTurn = true
    while (continueTurn) {
        when (player.nextAction(dealerTopCardValue)) {
            Action.HIT -> {
                println("Took the ${player.takeFrom(deck)}")
                if (player.busted || player.blackjack) {
                    println(if (player.blackjack) "Blackjack!" else "Busted...")
                    continueTurn = false
                }
            }
            Action.STAND -> {
                println("Ended their turn")
                continueTurn = false
            }
            Action.SPLIT -> {
                println("Split their hand")
                player.split(deck)

                println("\nStarting cards: ${player.firstTwoCards.map { it.name }}")
                continue
            }
            Action.DOUBLE -> {
                println("Doubled down with the ${player.takeFrom(deck)}")
                continueTurn = false
            }
        }
        println()
    }

    if (player.switchHands()) playerTurn(player, deck, dealerTopCardValue)
}

fun checkToContinue(): Boolean {
    print("Continue? ([Y]es or [N]o): ")
    return readLine()?.lowercase()?.get(0) == 'y'
}

fun main(args: Array<String>) {
    val deck = Deck()

    val dealer = Dealer()
    val players = args.map { EnhancedStrategyPlayer(it) }

    var continueGame = true
    while (continueGame) {
        deck.spreadTo(players + dealer)
        printlnln(dealer)

        if (!dealer.blackjack) for (player in players) playerTurn(player, deck, dealer.topCard.rank.rankValue)

        dealer.reveal()
        playerTurn(dealer, deck)

        val (winners, losers) = players.partition { it.beats(dealer) }

        println(dealer)
        println("Winners: ${if (winners.isEmpty()) "none" else winners.toString()}")
        printlnln("Losers: ${if (losers.isEmpty()) "none" else losers.toString()}")

        (players + dealer).map { it.reset() }
        continueGame = checkToContinue()
    }
}