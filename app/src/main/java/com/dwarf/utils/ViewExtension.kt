package com.dwarf.utils

import androidx.viewpager2.widget.ViewPager2

fun ViewPager2.getNextIndex() : Int {
    val maxPages = this.adapter?.itemCount
    val currentIndex = this.currentItem
    var selectedIndex = -1
    maxPages?.let {
        if (currentIndex + 1 < maxPages){
            selectedIndex = currentIndex + 1
        }

    }
    return selectedIndex
}

fun ViewPager2.getPreviousIndex() : Int {
    val currentPage = this.currentItem
    return if (currentPage - 1 >= 0){
        currentPage -1
    } else {
        -1
    }
}