package com.syarif.aidex_cgm.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


object TimeUtils {
    const val ONE_DAY_MILLS = (3600000 * 24).toLong()
    private const val ONE_MINUTE = 60000L
    private const val ONE_HOUR = 3600000L
    private const val ONE_DAY = 86400000L
    private const val ONE_WEEK = 604800000L
    private const val ONE_SECOND_AGO = "second age"
    private const val ONE_MINUTE_AGO = "min age"
    private const val ONE_HOUR_AGO = "hour age"
    private const val ONE_DAY_AGO = "day age"
    private const val ONE_MONTH_AGO = "month age"
    private const val ONE_YEAR_AGO = "year age"
    fun format(date: Date?): String {
        if (date == null) {
            return ""
        }
        val delta = Date().time - date.time
        if (delta < 1L * ONE_MINUTE) {
            val seconds = toSeconds(delta)
            return (if (seconds <= 0) 1 else seconds).toString() + ONE_SECOND_AGO
        }
        if (delta < 45L * ONE_MINUTE) {
            val minutes = toMinutes(delta)
            return (if (minutes <= 0) 1 else minutes).toString() + ONE_MINUTE_AGO
        }
        if (delta < 24L * ONE_HOUR) {
            val hours = toHours(delta)
            return (if (hours <= 0) 1 else hours).toString() + ONE_HOUR_AGO
        }
        if (delta < 48L * ONE_HOUR) {
            return "yesterday"
        }
        if (delta < 30L * ONE_DAY) {
            val days = toDays(delta)
            return (if (days <= 0) 1 else days).toString() + ONE_DAY_AGO
        }
        return if (delta < 12L * 4L * ONE_WEEK) {
            val months = toMonths(delta)
            (if (months <= 0) 1 else months).toString() + ONE_MONTH_AGO
        } else {
            val years = toYears(delta)
            (if (years <= 0) 1 else years).toString() + ONE_YEAR_AGO
        }
    }

    private fun toSeconds(date: Long): Long {
        return date / 1000L
    }

    private fun toMinutes(date: Long): Long {
        return toSeconds(date) / 60L
    }

    private fun toHours(date: Long): Long {
        return toMinutes(date) / 60L
    }

    private fun toDays(date: Long): Long {
        return toHours(date) / 24L
    }

    private fun toMonths(date: Long): Long {
        return toDays(date) / 30L
    }

    private fun toYears(date: Long): Long {
        return toMonths(date) / 365L
    }

    /**
     * ->2019-08-04 13:30:29
     */
    fun getNowTime(format: DateFormat): String? {
        var nowtime: String? = null
        val calendar = Calendar.getInstance()
        val dateNow = calendar.time
        val sdf = SimpleDateFormat(format.value, Locale.CHINA)
        nowtime = sdf.format(dateNow)
        return nowtime
    }

    fun getNowTime(): String? {
        return getNowTime(DateFormat.ONLY_MONTH_SEC)
    }

    enum class DateFormat {
        ONLY_MONTH_SEC {
            override val value: String?
                get() = "MM-dd HH:mm:ss"
        };

        abstract val value: String?
    }
}

