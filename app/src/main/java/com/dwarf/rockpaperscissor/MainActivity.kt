package com.dwarf.rockpaperscissor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.dwarf.enum.GameState
import com.dwarf.enum.HandGesture
import com.dwarf.enum.PlayerSide
import com.dwarf.manager.GameListener
import com.dwarf.manager.GameManager
import com.dwarf.manager.VersusComputerGameManager
import com.dwarf.model.Player
import com.dwarf.rockpaperscissor.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener, GameListener {
    private val binding : ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val gameManager: GameManager by lazy{
        VersusComputerGameManager(this)
    }

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()
        binding.ivPlayerPaper.setOnClickListener(this)
        binding.ivPlayerRock.setOnClickListener(this)
        binding.ivPlayerScissor.setOnClickListener(this)
        binding.ivRefresh.setOnClickListener(this)
        binding.tvStart.setOnClickListener(this)
        gameManager.initGame()

    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.iv_player_rock ->{
                gameManager.chooseHandGesture(0)
                Log.d( TAG, "OnClick = rock")
            }
            R.id.iv_player_paper ->{
                gameManager.chooseHandGesture(1)
                Log.d( TAG, "OnClick = paper")
            }
            R.id.iv_player_scissor ->{
                gameManager.chooseHandGesture(2)
                Log.d( TAG, "OnClick = scissor")
            }
            R.id.iv_refresh ->{
                gameManager.resetGame()
                Log.d( TAG, "OnClick = refresh")
            }
            R.id.tv_start ->{
                gameManager.playGame()
                Log.d( TAG, "OnClick = start")
            }
        }
    }

    override fun onPlayerStatusChanged(player: Player, iconDrawableRes: Int) {
        setCharacterMovement(player, iconDrawableRes)
    }

    private fun setCharacterMovement(player: Player, iconDrawableRes: Int) {
        val ivRock: ImageView?
        val ivPaper: ImageView?
        val ivScissor: ImageView?
        val bgDrawable = ContextCompat.getDrawable(this, iconDrawableRes)
        val defaultDrawable = ContextCompat.getDrawable(this, R.drawable.ic_background_image_white)
        if (player.playerSide == PlayerSide.PLAYER_ONE) {
            ivRock = binding.ivPlayerRock
            ivPaper = binding.ivPlayerPaper
            ivScissor = binding.ivPlayerScissor
        } else {
            ivRock = binding.ivComputerRock
            ivPaper = binding.ivComputerPaper
            ivScissor = binding.ivComputerScissor
        }

        when (player.handGesture) {
            HandGesture.ROCK -> {
                ivRock.background = bgDrawable
                ivPaper.background = defaultDrawable
                ivScissor.background = defaultDrawable
            }
            HandGesture.PAPER -> {
                ivRock.background = defaultDrawable
                ivPaper.background = bgDrawable
                ivScissor.background = defaultDrawable
            }
            HandGesture.SCISSORS -> {
                ivRock.background = defaultDrawable
                ivPaper.background = defaultDrawable
                ivScissor.background = bgDrawable

            }
        }
    }

    override fun onGameStateChanged(gameState: GameState) {
        binding.tvResult.text = ""
    }

    override fun onGameFinished(gameState: GameState, winner: Player) {
        when(gameState){
            GameState.IDLE ->  binding.tvResult.text = getString(R.string.text_rockPaperScissor)
            GameState.STARTED ->  binding.tvResult.text = getString(R.string.text_rockPaperScissor)
            GameState.FINISHED -> checkResult(winner)
        }

    }

    private fun checkResult(winner: Player) {
        when (winner.playerSide) {
            PlayerSide.BOTH -> {
                binding.tvResult.text = getString(R.string.text_draw)
            }
            PlayerSide.PLAYER_ONE -> {
                binding.tvResult.text = getString(R.string.text_you_win)
            }
            else -> {
                binding.tvResult.text = getString(R.string.text_you_lose)
            }
        }
    }


}