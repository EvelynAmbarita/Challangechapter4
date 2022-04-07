package com.binar.challenge4.utils

import java.text.SimpleDateFormat
import java.util.*

object DateConverter {
    fun convertMillisToString(timeMillis: Long): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timeMillis
        val sdf = SimpleDateFormat("dd/MMMM/yyyy", Locale.getDefault())
        return sdf.format(calendar.time)
    }
}