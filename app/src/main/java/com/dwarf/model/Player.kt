package com.dwarf.model

import com.dwarf.enum.HandGesture
import com.dwarf.enum.HandGestureState
import com.dwarf.enum.PlayerSide

data class Player(
    val playerSide: PlayerSide,
    var handGesture: HandGesture,
    var handGestureState: HandGestureState
)