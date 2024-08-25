package com.monetization.adsmain.commons

import android.app.Activity
import androidx.activity.ComponentActivity
import androidx.lifecycle.Lifecycle
import com.example.appupdate.AppUpdateHelper
import com.example.appupdate.SdkUpdateListener
import com.monetization.adsmain.widgets.AdsUiWidget
import com.monetization.bannerads.BannerAdSize
import com.monetization.bannerads.BannerAdType
import com.monetization.core.ui.AdsWidgetData
import com.monetization.core.ui.LayoutInfo
import com.monetization.core.ui.ShimmerInfo


fun AppUpdateHelper.checkAndStartUpdate(
    activity: ComponentActivity,
    onDismiss: (Boolean) -> Unit,
) {
    AppUpdateHelper.checkAndStartUpdate(activity,object : SdkUpdateListener {
        override fun onUpdateFailed(reason: String) {
            onDismiss.invoke(false)
        }

        override fun onUpdateSuccess() {
            onDismiss.invoke(true)

        }

        override fun onUpdateStarted() {
        }
    })

}


fun AdsUiWidget.sdkNativeAd(
    activity: Activity,
    adLayout: String,
    adKey: String,
    placementKey: String,
    lifecycle: Lifecycle,
    showShimmerLayout: ShimmerInfo = ShimmerInfo.GivenLayout(),
    adsWidgetData: AdsWidgetData? = null,
    requestNewOnShow: Boolean = false,
    oneTimeUse: Boolean = true,
    defaultEnable: Boolean = true,
) {
    apply {
        attachWithLifecycle(lifecycle = lifecycle, false)
        setWidgetKey(placementKey, adsWidgetData, defaultEnable)
        showNativeAdmob(
            activity = activity,
            adLayout = LayoutInfo.LayoutByName(adLayout),
            adKey = adKey,
            shimmerInfo = showShimmerLayout,
            oneTimeUse = oneTimeUse,
            requestNewOnShow = requestNewOnShow
        )
    }
}

fun AdsUiWidget.sdkBannerAd(
    activity: Activity,
    adKey: String,
    placementKey: String,
    lifecycle: Lifecycle,
    type: BannerAdType = BannerAdType.Normal(BannerAdSize.AdaptiveBanner),
    showShimmerLayout: ShimmerInfo = ShimmerInfo.GivenLayout(),
    adsWidgetData: AdsWidgetData? = null,
    requestNewOnShow: Boolean = false,
    oneTimeUse: Boolean = true,
    defaultEnable: Boolean = true,
) {
    apply {
        attachWithLifecycle(lifecycle = lifecycle, true)
        setWidgetKey(placementKey, adsWidgetData, defaultEnable)
        showBannerAdmob(
            activity = activity,
            bannerAdType = type,
            adKey = adKey,
            shimmerInfo = showShimmerLayout,
            oneTimeUse = oneTimeUse,
            requestNewOnShow = requestNewOnShow
        )
    }
}