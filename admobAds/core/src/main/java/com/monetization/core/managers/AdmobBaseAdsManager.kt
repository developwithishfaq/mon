package com.monetization.core.managers

import com.monetization.core.controllers.AdsControllerBaseHelper
import com.monetization.core.ad_units.core.AdType
import com.monetization.core.history.AdsManagerHistoryHelper

abstract class AdmobBaseAdsManager<T : AdsControllerBaseHelper>(adType: AdType) : AdsManager<T> {
    private val adsMap = HashMap<String, T>()


    override fun getAdController(key: String): T? {
        return adsMap[key]
    }

    override fun addNewController(controller: T) {
        val key = controller.getAdKey()
        if (adsMap[key] == null) {
            adsMap[key] = controller
            AdsManagerHistoryHelper.addController(controller)
        }
    }

    override fun removeController(adKey: String) {
        if (adsMap[adKey] != null) {
            adsMap.remove(adKey)
        }
    }

    override fun getAllController(): List<T> {
        return adsMap.values.toList()
    }
}