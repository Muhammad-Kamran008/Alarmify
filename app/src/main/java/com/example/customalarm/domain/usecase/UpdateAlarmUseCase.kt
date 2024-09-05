package com.example.customalarm.domain.usecase


import com.example.customalarm.data.local.entities.Alarm
import com.example.customalarm.domain.repository.AlarmRepository

class UpdateAlarmUseCase(private val alarmRepository: AlarmRepository) {

    fun execute(alarm: Alarm) {
        alarmRepository.update(alarm)
    }
}
