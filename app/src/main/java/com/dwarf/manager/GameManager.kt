package com.dwarf.manager

import android.util.Log
import com.dwarf.enum.GameState
import com.dwarf.enum.HandGesture
import com.dwarf.enum.HandGestureState
import com.dwarf.enum.PlayerSide
import com.dwarf.model.Player
import com.dwarf.ui.R

import kotlin.random.Random

interface GameManager {
    fun initGame()
    fun chooseHandGesture(index: Int)
    fun resetGame()
}

interface GameListener {
    fun onPlayerStatusChanged(player: Player, iconDrawableRes: Int)
    fun onGameStateChanged(gameState: GameState)
    fun onGameFinished(gameState: GameState, winner: Player)
}



open class RockPaperScissorGameManagerImpl(private val listener: GameListener) : GameManager {

    protected lateinit var playerOne: Player
    protected lateinit var playerTwo: Player
    protected lateinit var state: GameState

    companion object {
        private val TAG = RockPaperScissorGameManagerImpl::class.java.simpleName
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

    protected fun setGameState(newGameState: GameState) {
        state = newGameState
        listener.onGameStateChanged(state)
    }

    override fun chooseHandGesture(index: Int) {
        if (state != GameState.FINISHED) {
            setPlayerOneGesture(getPlayerPositionByOrdinal(index), HandGestureState.ACTIVE)
            startGame()
        }
    }

    protected fun setPlayerOneGesture(
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

    protected fun setPlayerTwoGesture(
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
        Log.d(TAG, "Player two choose $handGesture")
    }

    protected fun getPlayerPositionByOrdinal(index: Int): HandGesture {
        return HandGesture.values()[index]
    }

    override fun resetGame() {
        if (state == GameState.FINISHED) {
            initGame()
        }
    }

    protected fun startGame() {
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
            "You choose ${playerOne.handGesture} Player Two choose ${playerTwo.handGesture} winner ${winner.playerSide}"
        )
        setGameState(GameState.FINISHED)
        listener.onGameFinished(state, winner)
    }

    open fun getPlayerTwoPosition(): HandGesture {
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

class MultiplayerRockPaperScissorGameManager(listener: GameListener) : RockPaperScissorGameManagerImpl(listener){
    override fun initGame() {
        super.initGame()
        setGameState(GameState.PLAYER_ONE_TURN)
    }

    override fun getPlayerTwoPosition(): HandGesture {
        return playerTwo.handGesture
    }

    override fun chooseHandGesture(index: Int) {
        if (state == GameState.PLAYER_ONE_TURN){
            setPlayerOneGesture(getPlayerPositionByOrdinal(index), HandGestureState.ACTIVE)
            setGameState(GameState.PLAYER_TWO_TURN)
        } else if (state == GameState.PLAYER_TWO_TURN){
            setPlayerTwoGesture(getPlayerPositionByOrdinal(index), HandGestureState.ACTIVE)
            startGame()
        }
    }


    override fun resetGame() {
        initGame()
    }





}