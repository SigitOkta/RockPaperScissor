package com.dwarf.ui.splashscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import coil.load
import com.dwarf.ui.databinding.ActivitySplashScreenBinding
import com.dwarf.ui.landingpage.LandingPageActivity
import com.dwarf.ui.main.MainActivity

class SplashScreen : AppCompatActivity() {
    private var timer: CountDownTimer? = null

    private val binding: ActivitySplashScreenBinding by lazy {
        ActivitySplashScreenBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()
        loadImageSplash()
        setTimerSplashScreen()
    }

    private fun loadImageSplash() {
        binding.ivSplash.load("https://i.ibb.co/HC5ZPgD/splash-screen1.png"){
            crossfade(true)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (timer != null) {
            timer?.cancel()
            timer = null
        }
    }

    private fun setTimerSplashScreen() {
        timer = object : CountDownTimer(3000, 1000) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                val intent = Intent(this@SplashScreen, LandingPageActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
        }
        timer?.start()
    }
}