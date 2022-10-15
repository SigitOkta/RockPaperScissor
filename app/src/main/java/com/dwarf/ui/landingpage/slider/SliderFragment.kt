package com.dwarf.ui.landingpage.slider

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import coil.load
import coil.request.ImageRequest
import com.dwarf.model.SliderData
import com.dwarf.ui.R
import com.dwarf.ui.databinding.FragmentSliderBinding

class SliderFragment : Fragment() {

    private lateinit var binding: FragmentSliderBinding

    private var sliderData : SliderData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            sliderData = it.getParcelable(ARG_SLIDER_DATA)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSliderBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindSliderData()
    }

    private fun bindSliderData(){
        with(binding){
            tvDesc.text = sliderData?.desc.orEmpty()
            ivIntro.load(sliderData?.imgSlider){
                crossfade(true)
            }
        }
    }


    companion object {
        private const val ARG_SLIDER_DATA = "ARG_SLIDE_RDATA"
        @JvmStatic
        fun newInstance(sliderData: SliderData) =
            SliderFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_SLIDER_DATA,sliderData)
                }
            }
    }
}