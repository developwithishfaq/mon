package com.monetization.core

import android.app.Activity
import com.monetization.core.ad_units.core.AdType
import com.monetization.core.ad_units.core.AdUnit
import com.monetization.core.listeners.ControllersListener


interface AdsController {
    fun setAdEnabled(enabled: Boolean){}
    fun loadAd(activity: Activity, calledFrom: String, callback: AdsLoadingStatusListener?){}
    fun resetListener(activity: Activity){}
    fun setControllerListener(listener: ControllersListener?){}
    fun setListener(activity: Activity, callback: AdsLoadingStatusListener){}
    fun destroyAd(activity: Activity?){}
    fun getAdKey(): String
    fun getAdType(): AdType
    fun getAdIdsList(): List<String>
    fun isAdAvailable(): Boolean
    fun isAdRequesting(): Boolean
    fun isAdAvailableOrRequesting(): Boolean
    fun getAvailableAd(): AdUnit?

}