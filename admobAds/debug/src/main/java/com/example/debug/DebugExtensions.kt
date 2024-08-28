package com.example.debug

import java.text.SimpleDateFormat
import java.util.Locale


fun Long.formatMillisToTime(includeSeconds: Boolean = true, includeAmPm: Boolean = true): String {

    val format = if (includeSeconds && includeAmPm) {
        "hh:mm:ss a"
    } else if (includeSeconds) {
        "hh:mm:ss"
    } else if (includeAmPm) {
        "hh:mm a"
    } else {
        "hh:mm"
    }
    val sdf = SimpleDateFormat(format, Locale.getDefault())
    val date = java.util.Date(this)
    return sdf.format(date)
}