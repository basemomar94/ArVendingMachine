package com.ezfirecontrols.screensaverdemo

import android.net.Uri

data class ArItem(
    var image: Uri? = null,
    var isAvailable: Boolean = true,
    var itemName: String = "",
    var itemPrice: Int =0,
)
