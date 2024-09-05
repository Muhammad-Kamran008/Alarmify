package com.example.customalarm.presentation.ui.activities

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.customalarm.data.local.entities.Alarm
import com.example.customalarm.databinding.ActivityRingBinding
import com.example.customalarm.domain.repository.AlarmRepository
import com.example.customalarm.domain.usecase.CancelAlarmUseCase
import com.example.customalarm.domain.usecase.GetAlarmsUseCase
import com.example.customalarm.domain.usecase.InsertAlarmUseCase
import com.example.customalarm.domain.usecase.ScheduleAlarmUseCase
import com.example.customalarm.domain.usecase.UpdateAlarmUseCase
import com.example.customalarm.presentation.services.AlarmService
import com.example.customalarm.presentation.ui.viewmodels.AlarmViewModel
import java.util.Calendar
import java.util.Random




class RingActivity : AppCompatActivity() {
    private lateinit var alarmViewModel: AlarmViewModel

    private val binding by lazy{
        ActivityRingBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
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


        binding.dismiss.setOnClickListener {
            val intentService = Intent(
                applicationContext,
                AlarmService::class.java
            )
            applicationContext.stopService(intentService)
            finish()
        }

        binding.snooze.setOnClickListener {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = System.currentTimeMillis()
            calendar.add(Calendar.MINUTE, 10)

            val alarm = Alarm(
                Random().nextInt(Int.MAX_VALUE),
                calendar[Calendar.HOUR_OF_DAY],
                calendar[Calendar.MINUTE],
                System.currentTimeMillis(),
                isStarted = true,
                isRecurring = false,
                isMonday=false,
                isTuesday = false,
                isWednesday = false,
                isThursday = false,
                isFriday = false,
                isSaturday = false,
                isSunday = false
            )

            alarmViewModel.scheduleAlarm(alarm)

            val intentService = Intent(
                applicationContext,
                AlarmService::class.java
            )
            applicationContext.stopService(intentService)
            finish()
        }

        animateClock()
    }

    private fun animateClock() {
        val rotateAnimation = ObjectAnimator.ofFloat(binding.ringClock, "rotation", 0f, 20f, 0f, -20f, 0f)
        rotateAnimation.repeatCount = ValueAnimator.INFINITE
        rotateAnimation.setDuration(800)
        rotateAnimation.start()
    }
}

