package com.monetization.core.controllers

import android.app.Activity
import com.monetization.core.ad_units.core.AdType
import com.monetization.core.ad_units.core.AdUnit
import com.monetization.core.listeners.ControllersListener
import com.monetization.core.managers.AdsLoadingStatusListener


interface AdsController {
    fun setAdEnabled(enabled: Boolean){}
    fun loadAd(activity: Activity, calledFrom: String, callback: AdsLoadingStatusListener?){}
    fun resetListener(activity: Activity){}
    fun setControllerListener(listener: ControllersListener?){}
    fun setListener(activity: Activity, callback: AdsLoadingStatusListener){}
    fun destroyAd(activity: Activity?){}
    fun getAdKey(): String
    fun getAdId(): String
    fun getAdType(): AdType
    fun getAdIdsList(): List<String>
    fun isAdAvailable(): Boolean
    fun isAdRequesting(): Boolean
    fun isAdAvailableOrRequesting(): Boolean
    fun getAvailableAd(): AdUnit?

}