package com.monetization.adsmain.widgets

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import com.monetization.bannerads.BannerAdSize
import com.monetization.bannerads.BannerAdType
import com.monetization.bannerads.ui.BannerAdWidget
import com.monetization.core.commons.AdsCommons.logAds
import com.monetization.core.commons.SdkConfigs.getAdWidgetModel
import com.monetization.core.commons.SdkConfigs.isAdEnabled
import com.monetization.core.ui.AdsWidgetData
import com.monetization.core.ui.LayoutInfo
import com.monetization.core.ui.ShimmerInfo
import com.monetization.nativeads.ui.NativeAdWidget

class AdsUiWidget @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr), DefaultLifecycleObserver {

    init {
        logAds("AdWidget called", true)
    }

    private var nativeWidget: NativeAdWidget =
        NativeAdWidget(context, attrs, defStyleAttr)
    private var bannerWidget: BannerAdWidget =
        BannerAdWidget(context, attrs, defStyleAttr)

    private var widgetKey: String? = null
    private var adEnabled: Boolean = false
    private var adsWidgetDataModel: AdsWidgetData? = null

    fun isAdPopulated(forNative: Boolean): Boolean {
        return if (forNative) {
            nativeWidget.adPopulated()
        } else {
            bannerWidget.adPopulated()
        }
    }

    fun setWidgetKey(
        placementKey: String,
        adKey: String,
        model: AdsWidgetData?,
        defEnabled: Boolean = true
    ) {
        this.widgetKey = placementKey
        if (widgetKey == null || widgetKey?.isEmpty() == true) {
            throw IllegalArgumentException("Please Pass Placement Key")
        }

        val (enabled, widgetModel) = Pair(
            placementKey.isAdEnabled(adKey, defEnabled),
            placementKey.getAdWidgetModel(adKey, model)
        )

        adEnabled = enabled
        adsWidgetDataModel = widgetModel
        nativeWidget.setAdsWidgetData(
            adsWidgetData = adsWidgetDataModel,
            isValuesFromRemote = widgetModel.toString() != model.toString()
        )
    }

    fun attachWithLifecycle(lifecycle: Lifecycle, forBanner: Boolean = false) {
        if (lifecycle.currentState != Lifecycle.State.DESTROYED) {
            if (forBanner) {
                bannerWidget.attachWithLifecycle(lifecycle)
            } else {
                nativeWidget.attachWithLifecycle(lifecycle)
            }
        }
    }

    fun showNativeAdmob(
        activity: Activity,
        adLayout: LayoutInfo,
        adKey: String,
        shimmerInfo: ShimmerInfo = ShimmerInfo.GivenLayout(),
        oneTimeUse: Boolean = true,
        requestNewOnShow: Boolean = true,
    ) {
        removeAndAdNativeWidget()
        try {
            nativeWidget.showNativeAdmob(
                activity = activity,
                adKey = adKey,
                adLayout = adLayout,
                enabled = adEnabled,
                shimmerInfo = shimmerInfo,
                oneTimeUse = oneTimeUse,
                requestNewOnShow = requestNewOnShow
            )
        } catch (_: Exception) {

        }
    }


    fun showBannerAdmob(
        activity: Activity,
        adKey: String,
        bannerAdType: BannerAdType = BannerAdType.Normal(BannerAdSize.AdaptiveBanner),
        shimmerInfo: ShimmerInfo = ShimmerInfo.GivenLayout(),
        oneTimeUse: Boolean = true,
        requestNewOnShow: Boolean = true,
    ) {
        removeAndAdBannerWidget()
        try {
            bannerWidget.showBannerAdmob(
                activity = activity,
                adKey = adKey,
                bannerAdType = bannerAdType,
                enabled = adEnabled,
                shimmerInfo = shimmerInfo,
                oneTimeUse = oneTimeUse,
                requestNewOnShow = requestNewOnShow
            )
        } catch (_: Exception) {

        }
    }

    private fun removeAndAdNativeWidget() {
        try {
            (nativeWidget.parent as? ViewGroup)?.removeView(nativeWidget)
        } catch (_: Exception) {
        }
        try {
            addView(nativeWidget)
        } catch (_: Exception) {
        }
    }

    private fun removeAndAdBannerWidget() {
        try {
            (bannerWidget.parent as? ViewGroup)?.removeView(bannerWidget)
        } catch (_: Exception) {
        }
        try {
            addView(bannerWidget)
        } catch (_: Exception) {
        }
    }

    fun setInPause(check: Boolean, forBanner: Boolean) {
        if (forBanner) {
            bannerWidget.setInPause(check)
        } else {
            nativeWidget.setInPause(check)
        }
    }

}