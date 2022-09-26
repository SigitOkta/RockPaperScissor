package com.dwarf.manager

import android.util.Log
import com.dwarf.enum.GameState
import com.dwarf.enum.HandGesture
import com.dwarf.enum.HandGestureState
import com.dwarf.enum.PlayerSide
import com.dwarf.model.Player
import com.dwarf.rockpaperscissor.R
import kotlin.random.Random

interface GameManager {
    fun initGame()
    fun chooseHandGesture(index: Int)
    fun resetGame()
    fun playGame()
}

interface GameListener {
    fun onPlayerStatusChanged(player: Player, iconDrawableRes: Int)
    fun onGameStateChanged(gameState: GameState)
    fun onGameFinished(gameState: GameState, winner: Player)
}



class VersusComputerGameManager(private val listener: GameListener) : GameManager {

    private lateinit var playerOne: Player
    private lateinit var playerTwo: Player
    private lateinit var gameState: GameState

    companion object {
        private val TAG = VersusComputerGameManager::class.java.simpleName
    }

    override fun initGame() {
        setGameState(GameState.IDLE)
        playerOne = Player(PlayerSide.PLAYER_ONE, HandGesture.PAPER, HandGestureState.ACTIVE)
        playerTwo = Player(PlayerSide.PLAYER_TWO, HandGesture.PAPER, HandGestureState.ACTIVE)
        notifyPlayerDataChanged()
        setGameState(GameState.STARTED)
    }

    private fun notifyPlayerDataChanged() {
        listener.onPlayerStatusChanged(
            playerOne,
            getPlayerBackgroundByState(playerOne.handGestureState)
        )
        listener.onPlayerStatusChanged(
            playerTwo,
            getPlayerBackgroundByState(playerTwo.handGestureState)
        )
    }

    private fun setGameState(newGameState: GameState) {
        gameState = newGameState
        listener.onGameStateChanged(gameState)
    }

    override fun chooseHandGesture(index: Int) {
        if (gameState != GameState.FINISHED) {
            setPlayerOneGesture(getPlayerPositionByOrdinal(index), HandGestureState.ACTIVE)
        }
    }

    private fun setPlayerOneGesture(
        handGesture: HandGesture = playerOne.handGesture,
        handGestureState: HandGestureState = playerOne.handGestureState
    ) {
        playerOne.apply {
            this.handGesture = handGesture
            this.handGestureState = handGestureState
        }
        listener.onPlayerStatusChanged(
            playerOne,
            getPlayerBackgroundByState(playerOne.handGestureState)
        )
        Log.d(TAG, "You choose $handGesture")
    }

    private fun setPlayerTwoGesture(
        handGesture: HandGesture = playerTwo.handGesture,
        handGestureState: HandGestureState = playerTwo.handGestureState
    ) {
        playerTwo.apply {
            this.handGesture = handGesture
            this.handGestureState = handGestureState
        }
        listener.onPlayerStatusChanged(
            playerTwo,
            getPlayerBackgroundByState(playerTwo.handGestureState)
        )
        Log.d(TAG, "Computer choose $handGesture")
    }

    private fun getPlayerPositionByOrdinal(index: Int): HandGesture {
        return HandGesture.values()[index]
    }

    override fun resetGame() {
        if (gameState == GameState.FINISHED) {
            initGame()
        }
    }

    override fun playGame() {
        if (gameState != GameState.FINISHED) {
            startGame()
        }
    }


    private fun startGame() {
        playerTwo.apply {
            handGesture = getPlayerTwoPosition()
        }
        checkPlayerWinner()
    }

    private fun checkPlayerWinner() {
        val winner = if ((playerOne.handGesture.ordinal + 1) % 3 == playerTwo.handGesture.ordinal) {
            //Lose
            setPlayerOneGesture(handGestureState = HandGestureState.ACTIVE)
            setPlayerTwoGesture(handGestureState = HandGestureState.ACTIVE)
            playerTwo
        } else if (playerOne.handGesture == playerTwo.handGesture) {
            //Draw
            setPlayerOneGesture(handGestureState = HandGestureState.ACTIVE)
            setPlayerTwoGesture(handGestureState = HandGestureState.ACTIVE)
            Player(
                PlayerSide.BOTH,
                handGesture = playerOne.handGesture,
                handGestureState = HandGestureState.ACTIVE
            )
        } else {
            //WIN
            setPlayerOneGesture(handGestureState = HandGestureState.ACTIVE)
            setPlayerTwoGesture(handGestureState = HandGestureState.ACTIVE)
            playerOne
        }
        Log.d(TAG,
            "You choose ${playerOne.handGesture} Computer choose ${playerTwo.handGesture} winner ${winner.playerSide}"
        )
        setGameState(GameState.FINISHED)
        listener.onGameFinished(gameState, winner)
    }

    private fun getPlayerTwoPosition(): HandGesture {
        val randomPosition = Random.nextInt(0, until = HandGesture.values().size)
        return getPlayerPositionByOrdinal(randomPosition)
    }

    private fun getPlayerBackgroundByState(handGestureState: HandGestureState): Int {
        return when (handGestureState) {
            HandGestureState.ACTIVE -> R.drawable.ic_background_image
            HandGestureState.NONACTIVE -> R.drawable.ic_background_image_white
        }
    }
}