package com.monetization.core.ui

import androidx.annotation.LayoutRes

sealed class ShimmerInfo(
    open val hideShimmerOnFailure: Boolean = true,
) {

    data object None : ShimmerInfo()
    data class GivenLayout(
        override val hideShimmerOnFailure: Boolean = true
    ) : ShimmerInfo()

    data class ShimmerLayoutByName(
        val layoutName: String,
        override val hideShimmerOnFailure: Boolean = true,
    ) : ShimmerInfo()

    data class LayoutByXmlView(
        @LayoutRes val layoutRes: Int,
        override val hideShimmerOnFailure: Boolean = true,
    ) : ShimmerInfo()
}