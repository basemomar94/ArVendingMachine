package com.machine.screensaverdemo

import android.graphics.drawable.Drawable

data class ArItem(
    var image: Drawable? = null,
    var isAvailable: Boolean = true,
    var itemName: String = "",
    var itemPrice: Double = 0.0,
    var shelf: Int = 0
)
