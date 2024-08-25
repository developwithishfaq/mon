package com.monetization.core

interface AdsManager<T : AdsControllerBaseHelper> {
    fun getAdController(key: String): AdsController?
    fun addNewController( controller: T)
    fun removeController(adKey: String)
    fun getAllController(): List<T>
}