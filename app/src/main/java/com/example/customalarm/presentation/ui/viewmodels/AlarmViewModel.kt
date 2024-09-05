package com.example.customalarm.presentation.ui.viewmodels


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.customalarm.data.local.entities.Alarm
import com.example.customalarm.domain.usecase.CancelAlarmUseCase
import com.example.customalarm.domain.usecase.InsertAlarmUseCase
import com.example.customalarm.domain.usecase.UpdateAlarmUseCase
import com.example.customalarm.domain.usecase.GetAlarmsUseCase
import com.example.customalarm.domain.usecase.ScheduleAlarmUseCase
import kotlinx.coroutines.launch

class AlarmViewModel(
    private val insertAlarmUseCase: InsertAlarmUseCase,
    private val updateAlarmUseCase: UpdateAlarmUseCase,
    private val getAlarmsUseCase: GetAlarmsUseCase,
    private val scheduleAlarmUseCase:ScheduleAlarmUseCase,
    private val cancelAlarmUseCase: CancelAlarmUseCase
) : ViewModel() {

    val alarmsLiveData = getAlarmsUseCase.execute()

    fun insertAlarm(alarm: Alarm) {
        viewModelScope.launch {
            insertAlarmUseCase.execute(alarm)
        }
    }

    fun updateAlarm(alarm: Alarm) {
        viewModelScope.launch {
            updateAlarmUseCase.execute(alarm)
        }
    }


    fun scheduleAlarm(alarm: Alarm){
        viewModelScope.launch {
            scheduleAlarmUseCase.execute(alarm)

        }
    }


    fun cancelAlarm(alarm: Alarm){
        viewModelScope.launch {
            cancelAlarmUseCase.execute(alarm)
        }
    }

}




