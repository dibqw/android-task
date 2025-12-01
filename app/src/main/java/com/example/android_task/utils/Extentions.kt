package com.example.android_task.utils

import androidx.compose.ui.graphics.Color

fun String.convertToColor(): Color? {
    if (this.isEmpty()) {
        println("Input string is empty.")
        return null
    }

    val sanitizedHex = this.trim().removePrefix("#")

    // Add default opaque alpha (FF) if only RRGGBB is provided.
    val hexWithAlpha = when (sanitizedHex.length) {
        6 -> {
            "FF$sanitizedHex"
        }
        8 -> {
            sanitizedHex
        }
        else -> {
            println("Invalid hex length: $this")
            return null
        }
    }

    try {
        // Parse the AARRGGBB long value and pass it to the Color constructor.
        val colorLong = hexWithAlpha.toLong(16)
        // The Color constructor expects a signed 32-bit integer (Int) which works fine with the hex representation.
        return Color(colorLong)
    } catch (e: NumberFormatException) {
        println("Invalid hex value characters: $this, ${e.message}")
        return null
    }
}