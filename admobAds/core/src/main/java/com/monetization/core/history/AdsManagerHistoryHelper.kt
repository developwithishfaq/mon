package com.monetization.core.history

import com.monetization.core.AdsController
import com.monetization.core.models.AdmobAdInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object AdsManagerHistoryHelper {
    private val adsHistory = MutableStateFlow<List<AdmobAdInfo>>(emptyList())
    private val adsControllers = MutableStateFlow<List<AdsController>>(emptyList())
    fun getHistory() = adsHistory.asStateFlow()
    fun getControllers() = adsControllers.asStateFlow()

    fun addController(controller: AdsController) {
        val list = adsControllers.value.toMutableList()
        list.add(controller)
        adsControllers.value = list
    }

    fun addInHistory(admobAdInfo: AdmobAdInfo) {
        val list = adsHistory.value.toMutableList()
        val index = list.indexOfFirst {
            it.adKey == admobAdInfo.adKey && it.requestCount == admobAdInfo.requestCount
        }
        if (index != -1) {
            list.removeAt(index)
            list.add(index, admobAdInfo)
        } else {
            list.add(admobAdInfo)
        }
        adsHistory.value = list
    }
}