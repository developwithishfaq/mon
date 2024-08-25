package com.example.appupdate

interface AppUpdateLoadingListener {
    fun onUpdateAvailable(type: UpdateType)
    fun noNoUpdateAvailable()
    fun onFailure(reason: String)
}