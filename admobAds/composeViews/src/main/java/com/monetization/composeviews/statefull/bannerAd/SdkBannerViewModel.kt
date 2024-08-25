package com.monetization.composeviews.statefull.bannerAd

import androidx.lifecycle.ViewModel
import com.monetization.adsmain.widgets.AdsUiWidget
import com.monetization.composeviews.statefull.nativeAd.SdkNativeState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SdkBannerViewModel : ViewModel() {

    private val _state = MutableStateFlow(SdkNativeState())
    val state = _state.asStateFlow()

    fun updateState(widget: AdsUiWidget, adKey:String) {
        val mapp=state.value.adWidgetMap.toMutableMap()
        mapp[adKey]=widget
        _state.update {
            it.copy(
                adWidgetMap = mapp
            )
        }
    }

    fun setInPause(check: Boolean, key: String) {
        val mapp = state.value.adWidgetMap.toMutableMap()
        mapp[key]?.setInPause(check,false)
        _state.update {
            it.copy(
                adWidgetMap = mapp
            )
        }
    }

}