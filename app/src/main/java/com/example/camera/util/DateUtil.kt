package com.example.camera.util

import java.util.*

class DateUtil {

    fun getHours(): String {
        var calendar = Calendar.getInstance()
        return "${calendar.get(Calendar.HOUR_OF_DAY)}:${calendar.get(Calendar.MINUTE)}:" +
                "${calendar.get(Calendar.SECOND)}"
    }

}
