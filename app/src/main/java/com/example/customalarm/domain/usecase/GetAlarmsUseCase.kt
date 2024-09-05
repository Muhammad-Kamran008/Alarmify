package com.example.customalarm.domain.usecase

import androidx.lifecycle.LiveData
import com.example.customalarm.data.local.entities.Alarm
import com.example.customalarm.domain.repository.AlarmRepository

class GetAlarmsUseCase(private val alarmRepository: AlarmRepository) {

    fun execute(): LiveData<List<Alarm>> {
        return alarmRepository.alarmsLiveData
    }
}


