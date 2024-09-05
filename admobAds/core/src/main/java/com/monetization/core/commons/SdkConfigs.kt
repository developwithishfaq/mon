package com.monetization.core.commons

import com.monetization.core.BuildConfig
import com.monetization.core.ad_units.core.AdType
import com.monetization.core.commons.AdsCommons.logAds
import com.monetization.core.listeners.SdkListener

object SdkConfigs {

    private var isTestModeEnabled = BuildConfig.DEBUG
    private var disableAllAds = false

    fun isTestMode(): Boolean {
        return isTestModeEnabled
    }

    fun setTestModeEnabled(enable: Boolean) {
        isTestModeEnabled = enable
    }

    fun disableAllAds(disableAds: Boolean = true) {
        disableAllAds = disableAds
    }


    private var sdkListener: SdkListener? = null

    fun setListener(
        listener: SdkListener,
        testModeEnable: Boolean
    ) {
        sdkListener = listener
        setTestModeEnabled(testModeEnable)
    }

    fun getListener(): SdkListener? {
        if (sdkListener == null) {
            throw IllegalArgumentException("Please attach sdk listeners like SdkConfigs.setListener(this)")
        }
        return sdkListener
    }

    fun canShowAds(adKey: String, adType: AdType): Boolean {
        if (sdkListener == null) {
            throw IllegalArgumentException("Please attach sdk listeners like SdkConfigs.setListener(this)")
        } else if (disableAllAds) {
            logAds("All Ads Are Disabled by developer", true)
            return false
        } else {
            return sdkListener?.canShowAd(adType, adKey) ?: false
        }
    }

    fun canLoadAds(adKey: String, adType: AdType): Boolean {
        if (sdkListener == null) {
            throw IllegalArgumentException("Please attach sdk listeners like SdkConfigs.setListener(this)")
        } else if (disableAllAds) {
            logAds("All Ads Are Disabled by developer", true)
            return false
        } else {
            return sdkListener?.canLoadAd(adType, adKey) ?: false
        }
    }

}