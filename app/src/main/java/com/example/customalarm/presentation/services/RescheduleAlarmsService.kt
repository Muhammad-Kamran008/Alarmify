package com.example.customalarm.presentation.services
import android.content.Intent
import android.os.IBinder
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.Observer
import com.example.customalarm.domain.repository.AlarmRepository
import com.example.customalarm.domain.usecase.CancelAlarmUseCase
import com.example.customalarm.domain.usecase.GetAlarmsUseCase
import com.example.customalarm.domain.usecase.InsertAlarmUseCase
import com.example.customalarm.domain.usecase.ScheduleAlarmUseCase
import com.example.customalarm.domain.usecase.UpdateAlarmUseCase
import com.example.customalarm.presentation.ui.viewmodels.AlarmViewModel

class RescheduleAlarmsService : LifecycleService() {

    private lateinit var alarmViewModel: AlarmViewModel

    override fun onCreate() {
        super.onCreate()
        val alarmRepository = AlarmRepository(application)
        val getAlarmsUseCase = GetAlarmsUseCase(alarmRepository)
        val scheduleAlarmUseCase = ScheduleAlarmUseCase(application)
        val cancelAlarmUseCase = CancelAlarmUseCase(application)
        val insertAlarmUseCase = InsertAlarmUseCase(alarmRepository)
        val updateAlarmUseCase = UpdateAlarmUseCase(alarmRepository)

        alarmViewModel = AlarmViewModel(
            insertAlarmUseCase = insertAlarmUseCase,
            updateAlarmUseCase = updateAlarmUseCase,
            scheduleAlarmUseCase = scheduleAlarmUseCase,
            cancelAlarmUseCase = cancelAlarmUseCase,
            getAlarmsUseCase = getAlarmsUseCase
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        alarmViewModel.alarmsLiveData.observe(this, Observer { alarms ->
            alarms?.let {
                for (alarm in it) {
                    if (alarm.isStarted) {
                        alarmViewModel.scheduleAlarm(alarm)
                    }
                }
            }
        })

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        return null
    }
}


