package com.example.customalarm.presentation.services


import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.os.Vibrator
import androidx.core.app.NotificationCompat
import com.example.customalarm.App.Companion.CHANNEL_ID
import com.example.customalarm.R
import com.example.customalarm.presentation.ui.activities.RingActivity


class AlarmService : Service() {
    private var mediaPlayer: MediaPlayer? = null
    private var vibrator: Vibrator? = null

    override fun onCreate() {
        super.onCreate()

        mediaPlayer = MediaPlayer.create(this, R.raw.jingle)
        mediaPlayer!!.isLooping = true

        vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val notificationIntent = Intent(
            this,
            RingActivity::class.java
        )
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent,
            PendingIntent.FLAG_IMMUTABLE)

       // val alarmTitle = String.format("%s Alarm", intent.getStringExtra(TITLE))

        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Alarm")
            .setContentText("Ring Ring .. Ring Ring")
            .setSmallIcon(R.drawable.alarm_clock)
            .setContentIntent(pendingIntent)
            .build()

        mediaPlayer!!.start()

        val pattern = longArrayOf(0, 100, 1000)
        vibrator!!.vibrate(pattern, 0)

        startForeground(1, notification)

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer!!.stop()
        vibrator!!.cancel()
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}


