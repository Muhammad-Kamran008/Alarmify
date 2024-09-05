package com.example.customalarm.presentation.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.customalarm.MainActivity
import com.example.customalarm.data.local.entities.Alarm
import com.example.customalarm.databinding.ActivityAlarmsListBinding
import com.example.customalarm.domain.repository.AlarmRepository
import com.example.customalarm.domain.usecase.CancelAlarmUseCase
import com.example.customalarm.domain.usecase.GetAlarmsUseCase
import com.example.customalarm.domain.usecase.InsertAlarmUseCase
import com.example.customalarm.domain.usecase.ScheduleAlarmUseCase
import com.example.customalarm.domain.usecase.UpdateAlarmUseCase
import com.example.customalarm.presentation.ui.adapters.AlarmListAdapter
import com.example.customalarm.presentation.ui.listeners.OnToggleAlarmListener
import com.example.customalarm.presentation.ui.viewmodels.AlarmViewModel

class AlarmsListActivity : AppCompatActivity(), OnToggleAlarmListener {

    private val binding by lazy {
        ActivityAlarmsListBinding.inflate(layoutInflater)
    }
    private lateinit var alarmViewModel: AlarmViewModel
    private lateinit var alarmListAdapter: AlarmListAdapter

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

        alarmListAdapter = AlarmListAdapter(this)
        alarmViewModel.alarmsLiveData.observe(this, Observer { alarms ->
            alarms?.let {
                alarmListAdapter.setAlarms(it)
            }
        })

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = alarmListAdapter


        binding.addAlarm.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

        }


    }

    override fun onToggle(alarm: Alarm) {
//        if (alarm.isStarted) {
//
//
//            //  alarm.cancelAlarm(getContext())
//            //  alarmsListViewModel.update(alarm)
//        } else {
//
//            //  alarm.schedule(getContext())
//            //  alarmsListViewModel.update(alarm)
//        }
        if (alarm.isStarted) {
            alarmViewModel.cancelAlarm(alarm)
            alarm.isStarted = false
        } else {
            alarmViewModel.scheduleAlarm(alarm)
            alarm.isStarted = true
        }
        alarmViewModel.updateAlarm(alarm)
    }
}

