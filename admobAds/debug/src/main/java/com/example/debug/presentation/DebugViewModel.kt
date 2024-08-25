package com.example.debug.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.monetization.core.AdsController
import com.monetization.core.history.AdsManagerHistoryHelper
import com.monetization.core.models.AdmobAdInfo
import com.monetization.core.models.Failed
import com.monetization.core.models.Loaded
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DebugState(
    val items: List<AdmobAdInfo> = emptyList(),
    val controllers: List<ControllersInfo> = emptyList(),
)

fun List<ControllersInfo>.calculateMatchRates(): Int {
    val totalRequests = this.sumOf { it.noOfRequest }
    val totalLoadedAds = this.sumOf { it.noOfLoadedAds }
    if (totalRequests == 0) return 0
    return (totalLoadedAds * 100) / totalRequests
}


fun List<ControllersInfo>.calculateShowRates(): Int {
    val totalLoadedAds = this.sumOf { it.noOfLoadedAds }
    val totalImpressions = this.sumOf { it.noOfImpressions }
    if (totalLoadedAds == 0) return 0
    return (totalImpressions * 100) / totalLoadedAds
}

fun List<ControllersInfo>.getTotalRequests(): Int {
    return sumOf {
        it.noOfRequest
    }
}

fun List<ControllersInfo>.getTotalImpressions(): Int {
    return sumOf {
        it.noOfImpressions
    }
}

fun List<ControllersInfo>.getLoadedAdsCount(): Int {
    return sumOf {
        it.noOfLoadedAds
    }
}

fun List<ControllersInfo>.getNoFillsAdsCount(): Int {
    return sumOf {
        it.noOfNoFills
    }
}

fun List<ControllersInfo>.getTotalActiveAdUnits(): Int {
    return count {
        it.noOfRequest > 0
    }
}

data class ControllersInfo(
    val controller: AdsController,
    val noOfRequest: Int,
    val noOfLoadedAds: Int,
    val noOfNoFills: Int,
    val noOfImpressions: Int,
) {
    fun calculateShowRates(): Int {
        return if (noOfLoadedAds > 0) {
            (noOfImpressions / noOfLoadedAds) * 100
        } else {
            0
        }
    }

    fun calculateMatchRates(): Int {
        return if (noOfRequest > 0) {
            ((noOfLoadedAds / noOfRequest) * 100)
        } else {
            0
        }
    }
}

class DebugViewModel : ViewModel() {

    private val _state = MutableStateFlow(DebugState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            launch {
                AdsManagerHistoryHelper.getHistory().collectLatest { list ->
                    _state.update {
                        it.copy(
                            items = list
                        )
                    }
                    getControllers()
                }
            }
        }
    }

    fun getControllers() {
        viewModelScope.launch {
            val adsHistory = AdsManagerHistoryHelper.getHistory().value
            val controllers = AdsManagerHistoryHelper.getControllers().value

            val controllersWithStats = controllers.map { controller ->
                val myHistory = adsHistory.filter {
                    it.adKey == controller.getAdKey()
                }
                ControllersInfo(
                    controller = controller,
                    noOfRequest = myHistory.size,
                    noOfLoadedAds = myHistory.filter {
                        it.adFinalTime is Loaded
                    }.size,
                    noOfNoFills = myHistory.filter {
                        it.adFinalTime is Failed
                    }.size,
                    noOfImpressions = myHistory.filter {
                        it.adFinalTime is Loaded && it.adImpressionTime != null
                    }.size
                )
            }
            _state.update {
                it.copy(
                    controllers = controllersWithStats
                )
            }
        }
    }

}
















