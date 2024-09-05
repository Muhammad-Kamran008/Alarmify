package com.example.customalarm.domain.usecase

import android.app.Activity
import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.customalarm.data.local.entities.Alarm
import com.example.customalarm.presentation.receivers.AlarmBroadcastReceiver
import java.util.Calendar

class ScheduleAlarmUseCase(private val context: Context) {

//    fun execute(alarm: Alarm) {
//        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.SCHEDULE_EXACT_ALARM) != PackageManager.PERMISSION_GRANTED) {
//            showPermissionExplanation()
//            return
//        }
//
//
//    }


    fun execute(alarm: Alarm) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            if (!alarmManager.canScheduleExactAlarms()) {
                // Show permission explanation only if the context is an Activity
                if (context is Activity) {
                    context.runOnUiThread {
                        showPermissionExplanation(context)
                    }
                }
            }
        }
        try {
            val alarmManager = ContextCompat.getSystemService(context, AlarmManager::class.java)
            val intent = Intent(context, AlarmBroadcastReceiver::class.java).apply {
                putExtra(AlarmBroadcastReceiver.RECURRING, alarm.isRecurring)
                putExtra(AlarmBroadcastReceiver.MONDAY, alarm.isMonday)
                putExtra(AlarmBroadcastReceiver.TUESDAY, alarm.isTuesday)
                putExtra(AlarmBroadcastReceiver.WEDNESDAY, alarm.isWednesday)
                putExtra(AlarmBroadcastReceiver.THURSDAY, alarm.isThursday)
                putExtra(AlarmBroadcastReceiver.FRIDAY, alarm.isFriday)
                putExtra(AlarmBroadcastReceiver.SATURDAY, alarm.isSaturday)
                putExtra(AlarmBroadcastReceiver.SUNDAY, alarm.isSunday)
                //putExtra(AlarmBroadcastReceiver.TITLE, alarm.title)
            }

            val pendingIntent = PendingIntent.getBroadcast(context, alarm.alarmId, intent, PendingIntent.FLAG_IMMUTABLE)

            val calendar = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
                set(Calendar.HOUR_OF_DAY, alarm.hour)
                set(Calendar.MINUTE, alarm.minute)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)

                if (timeInMillis <= System.currentTimeMillis()) {
                    add(Calendar.DAY_OF_MONTH, 1)
                }
            }

            if (!alarm.isRecurring) {
                alarmManager?.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
            } else {
                val repeatInterval = AlarmManager.INTERVAL_DAY
                alarmManager?.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, repeatInterval, pendingIntent)
            }

            // Toast.makeText(context, "Alarm scheduled for ${calendar.time}", Toast.LENGTH_SHORT).show()
        } catch (e: SecurityException) {
            Toast.makeText(context, "Permission required to schedule exact alarms", Toast.LENGTH_SHORT).show()
        }
    }


    private fun showPermissionExplanation(activity: Activity) {
        AlertDialog.Builder(activity)
            .setTitle("Permission Required")
            .setMessage("To schedule exact alarms, this app needs permission to set alarms. Please grant this permission in the app settings.")
            .setPositiveButton("Go to Settings") { _, _ ->
                val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                } else {
                    null
                }
                intent?.let { activity.startActivity(it) }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}



