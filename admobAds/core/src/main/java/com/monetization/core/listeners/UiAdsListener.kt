package com.monetization.core.listeners

interface UiAdsListener {
    fun onAdClicked(key: String) {}
    fun onImpression(key: String) {}
    fun onAdRequested(key: String) {}
    fun onAdLoaded(key: String) {}
    fun onAdFailed(key: String, msg: String, code: Int) {}

}