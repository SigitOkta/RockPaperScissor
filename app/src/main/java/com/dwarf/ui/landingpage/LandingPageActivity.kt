package com.dwarf.ui.landingpage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isInvisible
import androidx.viewpager2.widget.ViewPager2
import com.dwarf.model.SliderData
import com.dwarf.ui.R
import com.dwarf.ui.databinding.ActivityLandingPageBinding
import com.dwarf.ui.landingpage.form.FormFragment
import com.dwarf.ui.landingpage.form.OnNameSubmittedListener
import com.dwarf.ui.landingpage.slider.SliderFragment
import com.dwarf.ui.main.MainActivity
import com.dwarf.ui.menu.MenuActivity
import com.dwarf.utils.ViewPagerAdapter
import com.dwarf.utils.getNextIndex
import com.dwarf.utils.getPreviousIndex
import com.google.android.material.snackbar.Snackbar

class LandingPageActivity : AppCompatActivity(), OnNameSubmittedListener {

    private val binding: ActivityLandingPageBinding by lazy {
        ActivityLandingPageBinding.inflate(layoutInflater)
    }

    private val pagerAdapter : ViewPagerAdapter by lazy {
        ViewPagerAdapter(supportFragmentManager, lifecycle)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()
        initFragmentViewPager()
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        binding.tvNext.setOnClickListener{
            navigateToNextFragment()
        }
        binding.tvPrevious.setOnClickListener {
            navigateToPreviousFragment()
        }
    }

    private fun navigateToPreviousFragment() {
        val nextIndex = binding.vpIntro.getPreviousIndex()
        if (nextIndex != -1){
            binding.vpIntro.setCurrentItem(nextIndex,true)
        }
    }

    private fun navigateToNextFragment() {
        val nextIndex = binding.vpIntro.getNextIndex()
        if (nextIndex != -1){
            binding.vpIntro.setCurrentItem(nextIndex,true)
        }
    }

    private fun initFragmentViewPager() {
        initAdapter()
        setupViewPager()
    }

    private fun initAdapter() {
        pagerAdapter.apply {
            addFragment(
                SliderFragment.newInstance(
                    SliderData(
                        desc = "Play Rock Paper Scissor with Player",
                        imgSlider = R.drawable.ic_landing_page_one
                    )
                )
            )
            addFragment(
                SliderFragment.newInstance(
                    SliderData(
                        desc = "Play Rock Paper Scissor with Computer",
                        imgSlider = R.drawable.ic_landing_page_two
                    )
                )
            )

            addFragment(FormFragment.newInstance().apply {
                setNameSubmittedListener(this@LandingPageActivity)
            })
        }
    }


    private fun setupViewPager(){
        binding.vpIntro.apply{
            adapter = pagerAdapter
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    when {
                        position == 0 -> {
                            binding.tvNext.isInvisible = false
                            binding.tvNext.isEnabled = true
                            binding.tvPrevious.isInvisible = true
                            binding.tvPrevious.isEnabled = false
                        }
                        position < pagerAdapter.getMaxIndex() -> {
                            binding.tvNext.isInvisible = false
                            binding.tvNext.isEnabled = true
                            binding.tvPrevious.isInvisible = false
                            binding.tvPrevious.isEnabled = true
                        }
                        position == pagerAdapter.getMaxIndex() -> {
                            binding.tvNext.isInvisible = true
                            binding.tvNext.isEnabled = false
                            binding.tvPrevious.isInvisible = false
                            binding.tvPrevious.isEnabled = true
                        }

                    }
                }
            })
        }
        binding.dotsIndicator.attachTo(binding.vpIntro)
    }

    override fun onNameSubmitted(name: String) {
        MenuActivity.startActivity(this,name)
    }
}