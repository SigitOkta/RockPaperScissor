package com.dwarf.model


import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SliderData(
    val desc :String,
    val imgSlider :Int,
) : Parcelable
