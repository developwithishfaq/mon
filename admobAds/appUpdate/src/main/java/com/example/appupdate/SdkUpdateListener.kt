package com.example.appupdate

interface SdkUpdateListener {
    fun onUpdateFailed(reason: String)
    fun onUpdateSuccess()
    fun onUpdateStarted()
}