package com.nalldev.gent.utils

import java.text.SimpleDateFormat
import java.util.Locale

object DateHelper {
    fun changeFormat(fromFormat: String, toFormat: String, date: String): String {
        return try {
            val originFormat = SimpleDateFormat(fromFormat, Locale.getDefault())
            val dateFormat = originFormat.parse(date)
            return dateFormat?.let {
                SimpleDateFormat(toFormat, Locale.getDefault()).format(it)
            } ?: run {
                String.format("-")
            }
        } catch (e : Exception) {
            String.format("-")
        }
    }
}