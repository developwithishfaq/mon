package com.example.debug

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK

object SdkDebugHelper {

    fun initDebugMode(context: Context, debugListener: DebugListener) {
        val detector = ShakeDetector(context) {
            if (debugListener.canLaunchDebugActivity()) {
                if (canShowDebugActivity) {
                    canShowDebugActivity = false
                    context.startActivity(Intent(context, DebugAdsHistoryActivity::class.java).run {
                        flags = FLAG_ACTIVITY_NEW_TASK
                        this
                    })
                }
            }
        }
        detector.start()
    }

    fun String.capFirstWord(): String {
        val text = drop(1).lowercase()
        return take(1) + text
    }
}