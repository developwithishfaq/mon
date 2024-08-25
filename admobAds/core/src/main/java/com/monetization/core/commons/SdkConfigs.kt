package com.monetization.core.commons

import com.monetization.core.BuildConfig
import com.monetization.core.ad_units.core.AdType
import com.monetization.core.listeners.SdkListener

object SdkConfigs {

    private var isTestModeEnabled = BuildConfig.DEBUG

    fun isTestMode(): Boolean {
        return isTestModeEnabled
    }

    fun setTestModeEnabled(enable: Boolean) {
        isTestModeEnabled = enable
    }


    private var sdkListener: SdkListener? = null

    fun setListener(listener: SdkListener) {
        sdkListener = listener
    }

    fun getListener(): SdkListener? {
        if (sdkListener == null) {
            throw IllegalArgumentException("Please attach sdk listeners like SdkConfigs.setListener(this)")
        }
        return sdkListener
    }

    fun canShowAd(adKey: String, adType: AdType): Boolean {
        if (sdkListener == null) {
            throw IllegalArgumentException("Please attach sdk listeners like SdkConfigs.setListener(this)")
        }
        return sdkListener?.canShowAd(adType, adKey) ?: false
    }

}