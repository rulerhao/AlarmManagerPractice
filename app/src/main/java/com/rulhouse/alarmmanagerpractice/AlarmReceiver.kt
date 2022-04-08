package com.rulhouse.alarmmanagerpractice

import android.R
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.activity.ComponentActivity


class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        showEasiestNotification(intent = intent, context = context)
    }

    private fun showEasiestNotification(intent: Intent, context: Context) {
        val bData = intent.extras
        if (bData!!["title"] == "activity_app") {
            val manager =
                context.getSystemService(ComponentActivity.NOTIFICATION_SERVICE) as NotificationManager
            val builder: Notification.Builder

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel("Day15", "Day15", NotificationManager.IMPORTANCE_HIGH)
                builder = Notification.Builder(context, "Day15")
                manager.createNotificationChannel(channel)
            } else {
                builder = Notification.Builder(context)
            }

            builder
                .setSmallIcon(R.drawable.sym_def_app_icon)
                .setContentTitle("Day15")
                .setContentText("Day15 Challenge")
                .setLargeIcon(
                    BitmapFactory.decodeResource(
                        context.resources,
                        R.mipmap.sym_def_app_icon
                    )
                )
                .setAutoCancel(true)
            val notification: Notification = builder.build()

            manager.notify(0, notification)
        }
    }
}