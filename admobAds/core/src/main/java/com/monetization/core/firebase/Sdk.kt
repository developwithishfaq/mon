package com.monetization.core.firebase

import android.content.Context
import android.os.Bundle
import com.monetization.core.history.AdsManagerHistoryHelper
import com.monetization.core.models.EventInfo
import video.downloader.remoteconfig.SdkFirebaseInternal

object Sdk {
    fun initFirebase(context: Context) {
        SdkFirebaseInternal.initializeDonotUse(context)
    }

    fun sendException(context: Exception) {
        SdkFirebaseInternal.sendExceptionDonotUse(context)
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