package com.example.customalarm.domain.usecase


import com.example.customalarm.data.local.entities.Alarm
import com.example.customalarm.domain.repository.AlarmRepository

class InsertAlarmUseCase(private val alarmRepository: AlarmRepository) {

    fun execute(alarm: Alarm) {
        alarmRepository.insert(alarm)
    }
}
