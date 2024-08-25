package com.monetization.core.commons

import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.os.Build
import org.json.JSONObject

object Utils {


    fun setColorFilterByColor(drawable: Drawable, color: Int) {
        try {
            drawable.colorFilter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                BlendModeColorFilter(color, BlendMode.SRC_ATOP)
            } else {
                PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP)
            }
        } catch (ignored: Exception) {
        }
    }


    fun JSONObject.getLongSafe(key: String): Long? {
        return try {
            getLong(key)
        } catch (_: Exception) {
            null
        }
    }

    fun JSONObject.getStringSafe(key: String): String? {
        return try {
            getString(key)
        } catch (_: Exception) {
            null
        }
    }
}