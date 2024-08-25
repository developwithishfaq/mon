package com.monetization.core

import android.app.Activity
import com.monetization.core.ad_units.core.AdType
import com.monetization.core.commons.AdsCommons
import com.monetization.core.commons.AdsCommons.logAds
import com.monetization.core.commons.SdkConfigs
import com.monetization.core.history.AdsManagerHistoryHelper
import com.monetization.core.listeners.ControllersListener
import com.monetization.core.models.AdmobAdInfo
import com.monetization.core.models.Failed
import com.monetization.core.models.Loaded

abstract class AdsControllerBaseHelper(
    private val adKey: String,
    private val adType: AdType,
    private val adIdsList: List<String>,
    private val listener: ControllersListener?,
) : AdsController {


    init {
        if (adIdsList.isEmpty()) {
            throw IllegalArgumentException("Please Provide Ids For key=${adKey}, $adType,")
        }
    }

    private var canRequestAd = true
    private var isAdEnabled = true
    private var indexOfId = 0
    private var adRequestCount = 0

    private var controllerListener: ControllersListener? = null

    private var adInfo: AdmobAdInfo? = null
    private var latestAdIdRequested: String = ""

    private var customDataMap = HashMap<String, String>()

    private var loadingStateListener: AdsLoadingStatusListener? = null

    fun getAdId(): String {
        val adId = AdsCommons.getAdId(indexOfId, adIdsList, adType) {
            indexOfId += 1
        }
        latestAdIdRequested = adId
        return adId
    }

    fun setDataMap(data: HashMap<String, String>) {
        customDataMap = data
    }

    init {
        this.controllerListener = listener
    }

    override fun setControllerListener(listener: ControllersListener?) {
        this.controllerListener = listener
    }


    private fun canLoadAd(): Boolean {
        val canLoadAd = SdkConfigs.getListener()?.canLoadAd(adType, adKey) ?: false
        if (canLoadAd.not()) {
            logAds(
                "You Blocked Request Of $adType Ads,Key=$adKey",
                true
            )
        }
        return canLoadAd
    }


    fun onAdRequested(
    ) {
        canRequestAd = false
        adRequestCount += 1
        adInfo = AdmobAdInfo(
            adRequestTime = System.currentTimeMillis(),
            adKey = adKey,
            adType = adType,
            adId = latestAdIdRequested,
            requestCount = adRequestCount,
            adImpressionTime = null
        )
        addInAdHistory()
        controllerListener?.onAdRequested(adKey, adType, customDataMap)
        logAds("$adType Ad Requested,Key=$adKey,Id=$latestAdIdRequested")
    }

    fun onAdClick() {
        controllerListener?.onAdClicked(adKey, adType, customDataMap)
        logAds("$adType Ad Clicked,Key=$adKey")
    }

    fun onImpression() {
        adInfo = adInfo?.copy(
            adImpressionTime = System.currentTimeMillis()
        )
        addInAdHistory()
        controllerListener?.onAdImpression(adKey, adType, customDataMap)
        logAds("$adType Ad Impression,Key=$adKey")
    }

    fun onLoaded() {
        canRequestAd = true
        adInfo = adInfo?.copy(
            adFinalTime = Loaded(System.currentTimeMillis())
        )
        addInAdHistory()
        loadingStateListener?.onAdLoaded(adKey)
        controllerListener?.onAdLoaded(adKey, adType, customDataMap)
        logAds("$adType Ad Loaded,Key=$adKey,id=$latestAdIdRequested")
    }

    fun onAdRevenue(
        value: Long, currencyCode: String, precisionType: Int,
    ) {
        controllerListener?.onAdRevenue(adKey, adType, value, currencyCode, precisionType)
        logAds("$adType Ad Revenue(value=$value,currency=$currencyCode,precision=$precisionType),Key=$adKey,id=$latestAdIdRequested")
    }


    fun onAdFailed(
        message: String, error: Int,
    ) {
        canRequestAd = true
        adInfo = adInfo?.copy(
            adFinalTime = Failed(
                System.currentTimeMillis(), message, error.toString()
            )
        )
        addInAdHistory()
        loadingStateListener?.onAdFailedToLoad(adKey, message, error)
        controllerListener?.onAdFailed(adKey, adType, message, error, customDataMap)
        logAds(
            "$adType Ad Failed To Load, msg=$message,code=$error, Key=$adKey,id=$latestAdIdRequested",
            true
        )
    }

    private fun addInAdHistory() {
        adInfo?.let {
            AdsManagerHistoryHelper.addInHistory(it)
        }
    }

    fun commonLoadAdChecks(
        callback: AdsLoadingStatusListener?,
    ): Boolean {
        if (canLoadAd().not()) {
            callback?.onAdFailedToLoad(adKey, "Ad Is Restricted To Load, key=$adKey,type=$adType")
            return false
        }
        logAds("$adType loadAd function called,enabled=$isAdEnabled,requesting=${isAdRequesting()},isAdAvailable=${isAdAvailable()}")
        this.loadingStateListener = callback
        if (isAdEnabled.not()) {
            loadingStateListener?.onAdFailedToLoad(adKey, "${adType} Ad is not enabled", -1)
            return false
        }
        if (isAdRequesting()) {
            return false
        }
        if (isAdAvailable()) {
            loadingStateListener?.onAdLoaded(adKey)
            return false
        }
        return true
    }

    override fun setAdEnabled(enabled: Boolean) {
        isAdEnabled = enabled
    }


    override fun resetListener(activity: Activity) {
        loadingStateListener = null
    }

    override fun setListener(activity: Activity, callback: AdsLoadingStatusListener) {
        loadingStateListener = callback
    }

    override fun getAdType(): AdType {
        return adType
    }

    override fun getAdKey(): String {
        return adKey
    }

    override fun getAdIdsList(): List<String> {
        return adIdsList
    }

    override fun isAdRequesting(): Boolean {
        return canRequestAd.not()
    }

    override fun isAdAvailableOrRequesting(): Boolean {
        return isAdRequesting() || isAdAvailable()
    }

}