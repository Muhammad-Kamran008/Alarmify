package com.example.customalarm.presentation.ui.listeners

import com.example.customalarm.data.local.entities.Alarm

interface OnToggleAlarmListener {
    fun onToggle(alarm:Alarm)
}

