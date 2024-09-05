package com.example.customalarm.core.utils

import android.widget.TimePicker

object TimePickerUtil {

    fun getTimePickerHour(tp: TimePicker?): Int {
        return tp?.hour ?: 0
    }

    fun getTimePickerMinute(tp: TimePicker?): Int {
        return tp?.minute ?: 0
    }
}

