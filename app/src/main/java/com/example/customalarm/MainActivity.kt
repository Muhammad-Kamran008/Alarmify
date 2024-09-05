package com.example.customalarm

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.customalarm.core.utils.TimePickerUtil
import com.example.customalarm.data.local.entities.Alarm
import com.example.customalarm.databinding.ActivityMainBinding
import com.example.customalarm.domain.repository.AlarmRepository
import com.example.customalarm.domain.usecase.CancelAlarmUseCase
import com.example.customalarm.domain.usecase.GetAlarmsUseCase
import com.example.customalarm.domain.usecase.InsertAlarmUseCase
import com.example.customalarm.domain.usecase.ScheduleAlarmUseCase
import com.example.customalarm.domain.usecase.UpdateAlarmUseCase
import com.example.customalarm.presentation.services.AlarmService
import com.example.customalarm.presentation.ui.activities.AlarmsListActivity
import com.example.customalarm.presentation.ui.viewmodels.AlarmViewModel
import com.google.android.material.snackbar.Snackbar
import java.util.Calendar
import java.util.Random

class MainActivity : AppCompatActivity() {

    private lateinit var alarmViewModel: AlarmViewModel
    private val PERMISSION_REQ_NOTIFICATION = 100

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
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
        setClickListeners()
    }


    private fun setClickListeners() {
        binding.scheduleAlarm.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                syncNotification()
            } else {
                scheduleAlarm()
            }
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
                isMonday = false,
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

    }

    private fun scheduleAlarm() {
        val alarmId = Random().nextInt(Int.MAX_VALUE)

        val alarm = Alarm(
            alarmId,
            TimePickerUtil.getTimePickerHour(binding.timePicker),
            TimePickerUtil.getTimePickerMinute(binding.timePicker),
            System.currentTimeMillis(),
            true,
            binding.recurring.isChecked,
            binding.Mon.isChecked,
            binding.Tue.isChecked,
            binding.Tue.isChecked,
            binding.Tue.isChecked,
            binding.Tue.isChecked,
            binding.Tue.isChecked,
            binding.Tue.isChecked
        )


        alarmViewModel.insertAlarm(alarm)
        alarmViewModel.scheduleAlarm(alarm)
        val intent = Intent(this, AlarmsListActivity::class.java)
        startActivity(intent)
        finish()
    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun syncNotification() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
            && checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), PERMISSION_REQ_NOTIFICATION
            )
        } else {
            scheduleAlarm()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQ_NOTIFICATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                scheduleAlarm()

            } else {
                Snackbar.make(
                    this,
                    binding.root,
                    "Permission is Not Granted",
                    Snackbar.LENGTH_SHORT
                )
                    .show()
            }
        }
    }

}

