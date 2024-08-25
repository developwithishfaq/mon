package com.example.debug

import java.text.SimpleDateFormat
import java.util.Locale


fun Long.formatMillisToTime(): String {
    val sdf = SimpleDateFormat("h:mm:ss a", Locale.getDefault())
    val date = java.util.Date(this)
    return sdf.format(date)
}