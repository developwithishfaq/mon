package com.monetization.bannerads.ui

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.facebook.shimmer.ShimmerFrameLayout
import com.monetization.bannerads.AdmobBannerAd
import com.monetization.bannerads.AdmobBannerAdsController
import com.monetization.bannerads.AdmobBannerAdsManager
import com.monetization.bannerads.BannerAdSize
import com.monetization.bannerads.BannerAdType
import com.monetization.core.managers.AdsLoadingStatusListener
import com.monetization.core.ad_units.core.AdType
import com.monetization.core.commons.AdsCommons.logAds
import com.monetization.core.commons.NativeConstants.inflateLayoutByName
import com.monetization.core.commons.NativeConstants.makeGone
import com.monetization.core.commons.NativeConstants.makeVisible
import com.monetization.core.commons.NativeConstants.removeViewsFromIt
import com.monetization.core.ui.ShimmerInfo
import com.monetization.core.ui.widgetBase.BaseAdsWidget

class BannerAdWidget @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : BaseAdsWidget<AdmobBannerAdsController>(context, attrs, defStyleAttr) {

    private var bannerAdType: BannerAdType = BannerAdType.Normal(BannerAdSize.AdaptiveBanner)

    private var bannerRefreshed = false

    fun showBannerAdmob(
        activity: Activity,
        adKey: String,
        bannerAdType: BannerAdType = BannerAdType.Normal(BannerAdSize.AdaptiveBanner),
        enabled: Boolean,
        shimmerInfo: ShimmerInfo = ShimmerInfo.GivenLayout(),
        oneTimeUse: Boolean = true,
        requestNewOnShow: Boolean = true,
    ) {
        this.bannerAdType = bannerAdType
        onShowAdCalled(
            adKey = adKey,
            activity = activity,
            oneTimeUse = oneTimeUse,
            requestNewOnShow = requestNewOnShow,
            enabled = enabled,
            shimmerInfo = shimmerInfo,
            adsManager = AdmobBannerAdsManager,
            adType = AdType.BANNER
        )
        logAds("showBannerAd called($key)=$bannerAdType,enable=$enabled,")
    }

    override fun loadAd() {
        (adsController as? AdmobBannerAdsController)?.loadBannerAd(
            activity = activity!!,
            bannerAdType = bannerAdType,
            calledFrom = "Base Banner Activity",
            callback = object : AdsLoadingStatusListener {
                override fun onAdLoaded(adKey: String) {
                    if (adLoaded) {
                        bannerRefreshed = true
                    }
                    adOnLoaded()
                }

                override fun onAdFailedToLoad(adKey: String, message: String, code: Int) {
                    adOnFailed()
                }
            }
        )
    }

    override fun populateAd() {
        adUnit?.let {
            (it as? AdmobBannerAd)?.populateAd(activity!!, this, onPopulated = {
                if (oneTimeUse) {
                    adsController?.destroyAd(activity!!)
                    if (bannerRefreshed) {
                        return@populateAd
                    }
                    if (requestNewOnShow) {
                        (adsController as AdmobBannerAdsController).loadBannerAd(
                            activity = activity!!,
                            calledFrom = "",
                            callback = null,
                            bannerAdType = bannerAdType
                        )
                    }
                }
                refreshLayout()
            })
        }
    }

    private fun destroyAndHideAd(activity: Activity) {
        makeVisible()
        removeAllViews()
        makeGone()
        (adUnit as? AdmobBannerAd)?.destroyAd(activity)
        adUnit = null
        adPopulated = false
        isLoadAdCalled = false
        isShowAdCalled = false
        adLoaded = false
    }


    override fun showShimmerLayout() {
        val info = shimmerInfo
        val shimmerLayout = LayoutInflater.from(activity)
            .inflate(com.monetization.core.R.layout.shimmer, null, false)
            ?.findViewById<ShimmerFrameLayout>(com.monetization.core.R.id.shimmerRoot)
        val shimmerView: ShimmerFrameLayout? = when (info) {
            is ShimmerInfo.GivenLayout -> {
                val layoutForShimmer = when (bannerAdType) {
                    is BannerAdType.Collapsible -> {
                        "adapter_banner_shimmer"
                    }

                    is BannerAdType.Normal -> {
                        when ((bannerAdType as BannerAdType.Normal).size) {
                            BannerAdSize.AdaptiveBanner -> "adapter_banner_shimmer"
                            BannerAdSize.MediumRectangle -> "rectangular_banner_shimmer"
                        }
                    }
                }
                val adLayout = layoutForShimmer.inflateLayoutByName(activity!!)
                shimmerLayout?.removeViewsFromIt()
                shimmerLayout?.addView(adLayout)
                shimmerLayout
            }

            is ShimmerInfo.ShimmerByView -> {
                info.layoutView?.let {
                    shimmerLayout?.removeViewsFromIt()
                    shimmerLayout?.addView(it)
                    shimmerLayout
                }
            }

            ShimmerInfo.None -> {
                null
            }
        }
        shimmerView?.let {
            removeAllViews()
            addView(shimmerView)
        }
    }
}