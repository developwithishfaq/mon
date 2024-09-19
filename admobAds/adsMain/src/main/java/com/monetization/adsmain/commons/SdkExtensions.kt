package com.monetization.adsmain.commons

import android.app.Activity
import androidx.activity.ComponentActivity
import androidx.lifecycle.Lifecycle
import com.example.appupdate.AppUpdateHelper
import com.example.appupdate.SdkUpdateListener
import com.example.rewadedad.AdmobRewardedAdsManager
import com.example.rewardedinterads.AdmobRewardedInterAdsManager
import com.monetization.adsmain.widgets.AdsUiWidget
import com.monetization.appopen.AdmobAppOpenAdsManager
import com.monetization.bannerads.AdmobBannerAdsManager
import com.monetization.bannerads.BannerAdSize
import com.monetization.bannerads.BannerAdType
import com.monetization.core.AdsController
import com.monetization.core.ad_units.core.AdType
import com.monetization.core.ui.AdsWidgetData
import com.monetization.core.ui.LayoutInfo
import com.monetization.core.ui.ShimmerInfo
import com.monetization.interstitials.AdmobInterstitialAdsManager
import com.monetization.nativeads.AdmobNativeAdsManager


fun AppUpdateHelper.checkAndStartUpdate(
    activity: ComponentActivity,
    onDismiss: (Boolean) -> Unit,
) {
    AppUpdateHelper.checkAndStartUpdate(activity, object : SdkUpdateListener {
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
    showNewAdEveryTime: Boolean = true,
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
            oneTimeUse = showNewAdEveryTime,
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
    showNewAdEveryTime: Boolean = true,
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
            oneTimeUse = showNewAdEveryTime,
            requestNewOnShow = requestNewOnShow
        )
    }
}
