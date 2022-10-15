package com.dwarf.ui.menu

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dwarf.ui.R
import com.dwarf.ui.databinding.ActivityMenuBinding
import com.dwarf.ui.main.MainActivity
import com.google.android.material.snackbar.Snackbar

class MenuActivity : AppCompatActivity() {

    private val binding: ActivityMenuBinding by lazy {
        ActivityMenuBinding.inflate(layoutInflater)
    }

    private val yourName: String by lazy{
        intent.getStringExtra(EXTRA_NAME).toString()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()
        binding.tvMenuPlayer.text = getString(R.string.placeholder_menu_vs_player,yourName)
        binding.tvMenuComputer.text = getString(R.string.placeholder_menu_vs_computer,yourName)
        Snackbar.make(binding.root,"Welcome $yourName", Snackbar.LENGTH_SHORT).show()
        setOnclickListeners()
    }

    private fun setOnclickListeners() {
        binding.ivMenuPlayer.setOnClickListener {
            MainActivity.startActivity(this,true, yourName)
        }
        binding.ivMenuComputer.setOnClickListener {
            MainActivity.startActivity(this,false, yourName)
        }
    }

    companion object {
        const val EXTRA_NAME = "EXTRA_NAME"
        fun startActivity(context: Context, name: String) {
            context.startActivity(Intent(context, MenuActivity::class.java).apply {
                putExtra(EXTRA_NAME, name)
            })
        }
    }
}