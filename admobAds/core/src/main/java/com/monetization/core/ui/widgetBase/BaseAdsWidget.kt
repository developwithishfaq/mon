package com.monetization.core.ui.widgetBase

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.monetization.core.ad_units.core.AdType
import com.monetization.core.ad_units.core.AdUnit
import com.monetization.core.commons.AdsCommons.getGoodName
import com.monetization.core.commons.AdsCommons.logAds
import com.monetization.core.commons.NativeConstants.makeGone
import com.monetization.core.commons.NativeConstants.makeVisible
import com.monetization.core.commons.SdkConfigs
import com.monetization.core.controllers.AdsController
import com.monetization.core.controllers.AdsControllerBaseHelper
import com.monetization.core.listeners.UiAdsListener
import com.monetization.core.managers.AdsLoadingStatusListener
import com.monetization.core.managers.AdsManager
import com.monetization.core.ui.AdsWidgetData
import com.monetization.core.ui.ShimmerInfo

abstract class BaseAdsWidget<T : AdsControllerBaseHelper> @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr), DefaultLifecycleObserver {

    var adUnit: AdUnit? = null
    var lifecycle: Lifecycle? = null
    var adsController: AdsController? = null
    var isShowAdCalled = false
    var adPopulated = false
    var isLoadAdCalled = false
    var activity: Activity? = null
    var key = ""
    private var isAdEnabled = false
    private var isViewInPause = false
    var adLoaded = false
    var oneTimeUse = false
    var requestNewOnShow = true
    var isValuesFromRemote = false
    var shimmerInfo: ShimmerInfo = ShimmerInfo.GivenLayout()
    var uiListener: UiAdsListener? = null

    var adsWidgetData: AdsWidgetData? = null
    private var adsManager: AdsManager<T>? = null

    fun setAdsWidgetData(adsWidgetData: AdsWidgetData?, isValuesFromRemote: Boolean) {
        logAds("setAdsWidgetData(fromRemote=$isValuesFromRemote) Widget Data=$adsWidgetData")
        this.isValuesFromRemote = isValuesFromRemote
        this.adsWidgetData = adsWidgetData
    }

    fun attachWithLifecycle(lifecycle: Lifecycle) {
        this.lifecycle = lifecycle
    }

    fun onShowAdCalled(
        adKey: String,
        activity: Activity,
        oneTimeUse: Boolean,
        requestNewOnShow: Boolean,
        enabled: Boolean,
        shimmerInfo: ShimmerInfo,
        adsManager: AdsManager<T>,
        adType: AdType,
        listener: UiAdsListener?
    ) {
        if (SdkConfigs.canShowAds(adKey, adType).not()) {
            logAds("Ad Showing is restricted against key=$adKey for $adType", true)
            makeGone()
            return
        }
        makeVisible()
        this.uiListener = listener
        this.key = adKey
        this.activity = activity
        this.oneTimeUse = oneTimeUse
        this.requestNewOnShow = requestNewOnShow
        this.isAdEnabled = enabled
        this.shimmerInfo = shimmerInfo
        this.adsManager = adsManager
        this.adLoaded = false
        this.isShowAdCalled = true
        this.adPopulated = false

        loadAdCalled(adsManager)
    }

    fun getAdsLoadingListener(): AdsLoadingStatusListener {
        return object : AdsLoadingStatusListener {
            override fun onAdRequested(adKey: String) {
                uiListener?.onAdRequested(adKey)
            }

            override fun onClicked(adKey: String) {
                uiListener?.onAdClicked(adKey)
            }

            override fun onAdLoaded(adKey: String) {
                uiListener?.onAdLoaded(adKey)
                if (adLoaded) {
                    return
                }
                adOnLoaded()
            }

            override fun onImpression(adKey: String) {
                uiListener?.onImpression(adKey)
            }

            override fun onAdFailedToLoad(adKey: String, message: String, code: Int) {
                uiListener?.onAdFailed(adKey, message, code)
                adOnFailed()
            }
        }
    }

    fun adOnLoaded() {
        adLoaded = true
        adUnit = adsController?.getAvailableAd()
        logAds(
            "${activity?.getGoodName()} Native=On Ad Loaded,Is Ad Ok=${adUnit != null}",
            isError = adUnit == null
        )
        if (isViewInPause.not()) {
            doPopulateAd()
        }
    }

    fun adOnFailed() {
        if (shimmerInfo.hideShimmerOnFailure) {
            makeGone()
        }
    }


    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        logAds("CustomNativeView onPause  key=$key")
        addOnPause()
    }

    private fun addOnPause() {
        isLoadAdCalled = false
        isViewInPause = true
        activity?.let { adsController?.resetListener(it) }
    }


    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        logAds("CustomNativeView onResume key=$key, isViewInPause=$isViewInPause")
        if (isViewInPause) {
            isViewInPause = false
            adOnResume()
        }
    }

    private fun adOnResume() {
        if (isShowAdCalled && isAdEnabled && isLoadAdCalled.not() && adPopulated.not()) {
            adsManager?.let {
                loadAdCalled(it)
            }
        }
    }

    private fun loadAdCalled(adsManager: AdsManager<T>) {
        isLoadAdCalled = true
        adsController = adsManager.getAdController(key)
        if (adsController == null) {
            logAds("Controller for $key, is not available")
            makeGone()
            return
        }
        if (adLoaded && adUnit != null) {
            doPopulateAd()
            return
        }
        if (isAdEnabled.not()) {
            makeGone()
            return
        }
        makeVisible()
        if (shimmerInfo != ShimmerInfo.None) {
            showShimmerLayout()
        }
        loadAd()
    }

    private fun doPopulateAd() {
        logAds("doPopulateAd(key=$key) isViewInPause=${isViewInPause}", isViewInPause)
        if (isViewInPause.not()) {
            adPopulated = true
            makeVisible()
            removeAllViews()
            populateAd()
        }
    }

    fun adPopulated(): Boolean {
        return adPopulated
    }

    fun setInPause(check: Boolean) {
        logAds("setInPause($key)=$check")
        isViewInPause = check
        if (isViewInPause.not()) {
            lifecycle?.addObserver(this)
            adOnResume()
        } else {
            lifecycle?.removeObserver(this)
            addOnPause()
        }
    }

    fun refreshLayout() {
        Handler(Looper.getMainLooper()).postDelayed({
            rootView.requestLayout()
        }, 50)
    }


    abstract fun populateAd()
    abstract fun showShimmerLayout()
    abstract fun loadAd()

}