package com.monetization.nativeads

import com.monetization.core.AdmobBaseAdsManager
import com.monetization.core.ad_units.core.AdType
import com.monetization.core.commons.TestAds


object AdmobNativeAdsManager : AdmobBaseAdsManager<AdmobNativeAdsController>(AdType.NATIVE) {
    init {
        addNewController(AdmobNativeAdsController("Test", listOf(TestAds.TestNativeId),null))
    }
}