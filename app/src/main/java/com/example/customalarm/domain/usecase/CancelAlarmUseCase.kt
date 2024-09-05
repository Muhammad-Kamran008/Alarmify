package com.example.customalarm.domain.usecase

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.example.customalarm.data.local.entities.Alarm
import com.example.customalarm.presentation.receivers.AlarmBroadcastReceiver

class CancelAlarmUseCase(private val context: Context) {

    fun execute(alarm: Alarm) {
        val alarmManager = ContextCompat.getSystemService(context, AlarmManager::class.java)
        val intent = Intent(context, AlarmBroadcastReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, alarm.alarmId, intent, PendingIntent.FLAG_IMMUTABLE)
        alarmManager?.cancel(pendingIntent)

        // Toast.makeText(context, "Alarm canceled for ${alarm.title}", Toast.LENGTH_SHORT).show()
    }
}


