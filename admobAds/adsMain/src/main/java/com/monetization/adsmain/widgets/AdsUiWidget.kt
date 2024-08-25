package com.monetization.adsmain.widgets

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import com.monetization.bannerads.BannerAdSize
import com.monetization.bannerads.BannerAdType
import com.monetization.bannerads.ui.BannerAdWidget
import com.monetization.core.commons.AdsCommons.logAds
import com.monetization.core.commons.Utils.getLongSafe
import com.monetization.core.commons.Utils.getStringSafe
import com.monetization.core.ui.AdsWidgetData
import com.monetization.core.ui.LayoutInfo
import com.monetization.core.ui.ShimmerInfo
import com.monetization.nativeads.ui.NativeAdWidget
import org.json.JSONObject
import video.downloader.remoteconfig.SdkRemoteConfigController

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

    fun setWidgetKey(key: String, adsWidgetData: AdsWidgetData?, defEnabled: Boolean = true) {
        this.widgetKey = key

        if (widgetKey == null || widgetKey?.isEmpty() == true) {
            throw IllegalArgumentException("Please Pass Placement Key")
        }

        adEnabled = SdkRemoteConfigController.getRemoteConfigBoolean(widgetKey ?: "", defEnabled)
        logAds("setWidgetKey called($key),enabled=$adEnabled")

        if (adsWidgetDataModel == null) {
            val placementKey = (key ?: "") + "_Placement"
            val widgetData =
                SdkRemoteConfigController.getRemoteConfigString(placementKey)
            adsWidgetDataModel = if (widgetData.isNotBlank()) {
                try {
                    JSONObject(widgetData).run {
                        val enabled = getLongSafe("enabled")?.toInt()
                        if (enabled != null && enabled == 1) {
                            AdsWidgetData(
                                enabled = enabled,
                                refreshTime = getLongSafe("refreshTime"),
                                margings = getStringSafe("margins"),
                                adCtaHeight = getLongSafe("adCtaHeight")?.toFloat(),
                                adCtaTextSize = getLongSafe("adCtaTextSize")?.toFloat(),
                                adMediaViewHeight = getLongSafe("adMediaViewHeight")?.toFloat(),
                                adIconHeight = getLongSafe("adIconHeight")?.toFloat(),
                                adIconWidth = getLongSafe("adIconWidth")?.toFloat(),
                                adHeadlineTextSize = getLongSafe("adHeadlineTextSize")?.toFloat(),
                                adBodyTextSize = getLongSafe("adBodyTextSize")?.toFloat(),
                                adCtaBgColor = getStringSafe("adCtaBgColor"),
                                adLayout = getStringSafe("adLayout"),
                                adCtaTextColor = getStringSafe("adCtaTextColor"),
                                adHeadLineTextColor = getStringSafe("adHeadLineTextColor"),
                                adBodyTextColor = getStringSafe("adBodyTextColor"),
                                adAttrTextColor = getStringSafe("adAttrTextColor"),
                                adAttrBgColor = getStringSafe("adAttrBgColor"),
                            )
                        } else {
                            adsWidgetData
                        }
                    }
                } catch (_: Exception) {
                    adsWidgetData
                }
            } else {
                adsWidgetData
            }
            nativeWidget.setAdsWidgetData(
                adsWidgetData = adsWidgetDataModel,
                isValuesFromRemote = widgetData.isNotBlank()
            )
        }
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

        addView(nativeWidget)
        nativeWidget.showNativeAdmob(
            activity = activity,
            adKey = adKey,
            adLayout = adLayout,
            enabled = adEnabled,
            shimmerInfo = shimmerInfo,
            oneTimeUse = oneTimeUse,
            requestNewOnShow = requestNewOnShow
        )
    }

    fun showBannerAdmob(
        activity: Activity,
        adKey: String,
        bannerAdType: BannerAdType = BannerAdType.Normal(BannerAdSize.AdaptiveBanner),
        shimmerInfo: ShimmerInfo = ShimmerInfo.GivenLayout(),
        oneTimeUse: Boolean = true,
        requestNewOnShow: Boolean = true,
    ) {
        addView(bannerWidget)
        bannerWidget.showBannerAdmob(
            activity = activity,
            adKey = adKey,
            bannerAdType = bannerAdType,
            enabled = adEnabled,
            shimmerInfo = shimmerInfo,
            oneTimeUse = oneTimeUse,
            requestNewOnShow = requestNewOnShow
        )
    }

    fun setInPause(check: Boolean, forBanner: Boolean) {
        if (forBanner) {
            bannerWidget.setInPause(check)
        } else {
            nativeWidget.setInPause(check)
        }
    }

}