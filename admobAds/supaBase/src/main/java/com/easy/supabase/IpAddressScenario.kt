package com.easy.supabase

sealed class IpAddressScenario {
    data object None : IpAddressScenario()
    data object LoadEveryTime : IpAddressScenario()
    data object OneTimeOnly : IpAddressScenario()
    data class RequestAfterEvery(val millis: Long) : IpAddressScenario()
}