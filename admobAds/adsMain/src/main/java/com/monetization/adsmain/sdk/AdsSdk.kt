package com.monetization.adsmain.sdk

import android.content.Context
import com.google.android.gms.ads.MobileAds
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import video.downloader.remoteconfig.SdkFirebase


class AdsSdk(
) {
    private var isSdkInitialized = false
    private var isFirebaseInitialized = false
    fun initAdsSdk(
        context: Context,
        initAdsSdk: Boolean = true,
        initFirebase: Boolean = true,
        onInitialized: () -> Unit,
    ) {
        if (initAdsSdk) {
            initAdmobSdk(context, onInitialized)
        }
        if (initFirebase) {
            initFirebase(context, onInitialized)
        }
    }

    private fun initAdmobSdk(context: Context, onInitialized: () -> Unit) {
        if (isSdkInitialized) {
            return
        }
        isSdkInitialized = true
        CoroutineScope(Dispatchers.IO).launch {
            MobileAds.initialize(context) {
                CoroutineScope(Dispatchers.Main).launch {
                    onInitialized.invoke()
                }
            }
            MobileAds.setAppMuted(true)
        }
    }

    private fun initFirebase(context: Context, onInitialized: () -> Unit) {
        if (isFirebaseInitialized) {
            return
        }
        isFirebaseInitialized = true
        SdkFirebase.initialize(context)
        onInitialized.invoke()
    }
}