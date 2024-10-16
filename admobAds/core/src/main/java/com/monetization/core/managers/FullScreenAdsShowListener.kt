package com.monetization.core.managers

interface FullScreenAdsShowListener {
    fun onAdDismiss(adKey: String, adShown: Boolean = false, rewardEarned: Boolean = false) {}
    fun onAdShown(adKey: String) {}
    fun onAdShownFailed(adKey: String) {}
    fun onAdClick(adKey: String) {}
    fun onShowBlackBg(adKey: String, show: Boolean) {}
}