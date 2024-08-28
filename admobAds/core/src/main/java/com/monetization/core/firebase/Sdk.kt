package com.monetization.core.firebase

import android.content.Context
import android.os.Bundle
import android.util.Log
import com.monetization.core.history.AdsManagerHistoryHelper
import com.monetization.core.models.EventInfo
import com.monetization.core.models.LogInfo
import video.downloader.remoteconfig.SdkFirebaseInternal

object Sdk {
    fun initFirebase(context: Context) {
        SdkFirebaseInternal.initializeDonotUse(context)
    }

    fun sendException(context: Exception) {
        SdkFirebaseInternal.sendExceptionDonotUse(context)
    }

    fun log(tag: String, msg: String, isError: Boolean = false) {
        if (isError) {
            Log.e(tag, msg)
        } else {
            Log.d(tag, msg)
        }
        AdsManagerHistoryHelper.addLog(LogInfo(msg, tag, isError))
    }

    fun sendEvent(context: Context, message: String, bundle: Bundle = Bundle()) {
        AdsManagerHistoryHelper.addEvents(
            EventInfo(
                message,
                System.currentTimeMillis()
            )
        )
        SdkFirebaseInternal.sendEventDonotUse(context, message, bundle)
    }
}