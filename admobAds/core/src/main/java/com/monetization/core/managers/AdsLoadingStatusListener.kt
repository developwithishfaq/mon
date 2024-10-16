package com.monetization.core.managers

interface AdsLoadingStatusListener {
    fun onAdLoaded(adKey: String)
    fun onAdFailedToLoad(adKey: String,message: String = "", code: Int = -1)

}