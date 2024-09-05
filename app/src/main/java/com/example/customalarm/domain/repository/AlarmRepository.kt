package com.example.customalarm.domain.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.customalarm.data.local.dao.AlarmDao
import com.example.customalarm.data.local.db.AlarmDatabase
import com.example.customalarm.data.local.entities.Alarm

class AlarmRepository(application: Application) {
    private val alarmDao: AlarmDao
    val alarmsLiveData: LiveData<List<Alarm>>

    init {
        val db = AlarmDatabase.getDatabase(application)
        alarmDao = db.alarmDao()
        alarmsLiveData = alarmDao.alarms
    }

    fun insert(alarm: Alarm) {
        AlarmDatabase.databaseWriteExecutor.execute {
            alarmDao.insert(alarm)
        }
    }

    fun update(alarm: Alarm) {
        AlarmDatabase.databaseWriteExecutor.execute {
            alarmDao.update(alarm)
        }
    }
}



