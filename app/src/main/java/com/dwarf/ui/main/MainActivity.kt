package com.dwarf.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.dwarf.enum.GameState
import com.dwarf.enum.HandGesture
import com.dwarf.enum.PlayerSide
import com.dwarf.manager.GameListener
import com.dwarf.manager.GameManager
import com.dwarf.manager.MultiplayerRockPaperScissorGameManager
import com.dwarf.manager.RockPaperScissorGameManagerImpl
import com.dwarf.model.Player
import com.dwarf.ui.R
import com.dwarf.ui.databinding.ActivityMainBinding
import com.dwarf.ui.databinding.CustomDialogBinding
import com.dwarf.ui.menu.MenuActivity
import com.dwarf.ui.menu.MenuActivity.Companion.EXTRA_NAME


class MainActivity : AppCompatActivity(), View.OnClickListener, GameListener {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val isUsingMultiplayerMode: Boolean by lazy {
        intent.getBooleanExtra(EXTRA_MULTIPLAYER_MODE, false)
    }

    private val nameUser: String by lazy {
        intent.getStringExtra(EXTRA_NAME_MAIN).toString()
    }

    private val gameManager: GameManager by lazy {
        if (isUsingMultiplayerMode)
            MultiplayerRockPaperScissorGameManager(this)
        else
            RockPaperScissorGameManagerImpl(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()
        binding.tvName.text = nameUser
        binding.ivPlayerPaper.setOnClickListener(this)
        binding.ivPlayerRock.setOnClickListener(this)
        binding.ivPlayerScissor.setOnClickListener(this)
        binding.tvAction.setOnClickListener(this)
        binding.ivClose.setOnClickListener(this)
        gameManager.initGame()

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_player_rock -> {
                gameManager.chooseHandGesture(0)
                Toast.makeText(this,"$nameUser choose Rock",Toast.LENGTH_SHORT).show()
                Log.d(TAG, "OnClick = rock")
            }
            R.id.iv_player_paper -> {
                gameManager.chooseHandGesture(1)
                Toast.makeText(this,"$nameUser choose Paper",Toast.LENGTH_SHORT).show()
                Log.d(TAG, "OnClick = paper")
            }
            R.id.iv_player_scissor -> {
                gameManager.chooseHandGesture(2)
                Toast.makeText(this,"$nameUser choose Scissor",Toast.LENGTH_SHORT).show()
                Log.d(TAG, "OnClick = scissor")
            }
            R.id.iv_computer_rock -> {
                gameManager.chooseHandGesture(0)
                Toast.makeText(this,"Player Two choose Rock",Toast.LENGTH_SHORT).show()
                Log.d(TAG, "OnClick = player_two_rock")
            }
            R.id.iv_computer_paper -> {
                gameManager.chooseHandGesture(1)
                Toast.makeText(this,"Player Two choose Paper",Toast.LENGTH_SHORT).show()
                Log.d(TAG, "OnClick = player_two_rock")
            }
            R.id.iv_computer_scissor -> {
                gameManager.chooseHandGesture(2)
                Toast.makeText(this,"Player Two choose Scissor",Toast.LENGTH_SHORT).show()
                Log.d(TAG, "OnClick = player_two_rock")
            }
            R.id.tv_action -> {
                gameManager.resetGame()
                Log.d(TAG, "OnClick = restart")
            }
            R.id.iv_close -> {
                backToMenuActivity()
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
        when (gameState) {
            GameState.IDLE -> {
                binding.tvResult.text = getString(R.string.text_rockPaperScissor)
                setCharacterVisibility(isPlayerOneVisible = true, isPlayerTwoVisible = true)
            }
            GameState.STARTED -> {
                binding.tvResult.text = getString(R.string.text_rockPaperScissor)
                setCharacterVisibility(isPlayerOneVisible = true, isPlayerTwoVisible = true)
            }
            GameState.FINISHED -> {
                setCharacterVisibility(isPlayerOneVisible = true, isPlayerTwoVisible = true)
            }
            GameState.PLAYER_ONE_TURN -> {
                binding.tvResult.text = getString(R.string.text_player_one_turn)
                setCharacterVisibility(isPlayerOneVisible = true, isPlayerTwoVisible = false)
            }
            GameState.PLAYER_TWO_TURN -> {
                binding.ivComputerRock.setOnClickListener(this)
                binding.ivComputerScissor.setOnClickListener(this)
                binding.ivComputerPaper.setOnClickListener(this)
                binding.tvResult.text = getString(R.string.text_player_two_turn)
                setCharacterVisibility(isPlayerOneVisible = false, isPlayerTwoVisible = true)
            }
        }
    }

    private fun setCharacterVisibility(isPlayerOneVisible: Boolean, isPlayerTwoVisible: Boolean) {
        binding.llPlayer.isVisible = isPlayerOneVisible
        binding.llComputer.isVisible = isPlayerTwoVisible
    }

    override fun onGameFinished(gameState: GameState, winner: Player) {
        when (gameState) {
            GameState.IDLE -> binding.tvResult.text = getString(R.string.text_rockPaperScissor)
            GameState.STARTED -> binding.tvResult.text = getString(R.string.text_rockPaperScissor)
            GameState.PLAYER_ONE_TURN -> binding.tvResult.text = getString(R.string.text_player_two_turn)
            GameState.PLAYER_TWO_TURN -> binding.tvResult.text = ""
            GameState.FINISHED -> checkResult(winner)

        }

    }

    private fun checkResult(winner: Player) {
        when (winner.playerSide) {
            PlayerSide.BOTH -> {
                showDialogResult(winner)
                binding.tvResult.text = getString(R.string.text_draw)
            }
            PlayerSide.PLAYER_ONE -> {
                showDialogResult(winner)
                binding.tvResult.text = getString(R.string.text_you_win)
            }
            else -> {
                showDialogResult(winner)
                binding.tvResult.text = getString(R.string.text_you_lose)
            }
        }
    }

    private fun showDialogResult(winner: Player) {
        val binding = CustomDialogBinding.inflate(LayoutInflater.from(this))
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setView(binding.root)
        dialogBuilder.setCancelable(false)
        val dialog = dialogBuilder.create()
        when (winner.playerSide) {
            PlayerSide.BOTH -> {
                binding.tvWinner.text = getString(R.string.text_draw)
            }
            PlayerSide.PLAYER_ONE -> {
                binding.tvWinner.text = getString(R.string.placeholder_win,nameUser)
            }
            else -> {
                binding.tvWinner.text = getString(R.string.text_player_two_win)
            }
        }

        binding.btnPlayAgain.setOnClickListener {
            gameManager.resetGame()
            dialog.dismiss()
        }
        binding.btnBackToMenu.setOnClickListener {
            backToMenuActivity()
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun backToMenuActivity() {
        val intent = Intent(this, MenuActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        intent.putExtra(EXTRA_NAME, nameUser)
        startActivity(intent)
        finish()
    }

    companion object {
        private val TAG = MainActivity::class.java.simpleName
        const val EXTRA_MULTIPLAYER_MODE = "EXTRA_MULTIPLAYER_MODE"
        const val EXTRA_NAME_MAIN = "EXTRA_NAME_MAIN"
        fun startActivity(context: Context, isUsingMultiplayerMode: Boolean, name : String) {
            context.startActivity(Intent(context, MainActivity::class.java).apply {
                putExtra(EXTRA_MULTIPLAYER_MODE, isUsingMultiplayerMode)
                putExtra(EXTRA_NAME_MAIN, name)
            })
        }
    }

}