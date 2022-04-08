package com.rulhouse.alarmmanagerpractice

import android.R
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rulhouse.alarmmanagerpractice.ui.theme.AlarmManagerPracticeTheme
import java.util.*


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AlarmManagerPracticeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Greeting("Android") {
                        showEasiestNotification(this)
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        val alarmManager: AlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val hasPermission: Boolean = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            alarmManager.canScheduleExactAlarms()
        } else {
            true
        }

        if (!hasPermission) {
            val intent = Intent().apply {
                action = Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
            }

            startActivity(intent)
        }

        val cal: Calendar = GregorianCalendar(TimeZone.getTimeZone("GMT+8:00")) //取得時間
        for (i in 0..9) {
            cal.add(Calendar.MINUTE, 0) //加一分鐘
            cal.add(Calendar.SECOND, 5) //設定秒數為0
            MainActivity.add_alarm(this, cal) //註冊鬧鐘
        }

    }

    fun showEasiestNotification(context: Context) {
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

    companion object {
        /***    加入(與系統註冊)鬧鐘     */
        fun add_alarm(context: Context, cal: Calendar) {
            val intent = Intent(context, AlarmReceiver::class.java)
            // 以日期字串組出不同的 category 以添加多個鬧鐘
            intent.addCategory(
                "ID." + java.lang.String.valueOf(cal.get(Calendar.MONTH)) + "." + java.lang.String.valueOf(
                    cal.get(Calendar.DATE)
                ) + "-" + java.lang.String.valueOf(
                    cal.get(Calendar.HOUR_OF_DAY)
                ) + "." + java.lang.String.valueOf(cal.get(Calendar.MINUTE)) + "." + java.lang.String.valueOf(
                    cal.get(Calendar.SECOND)
                )
            )
            val AlarmTimeTag =
                "Alarmtime " + java.lang.String.valueOf(cal.get(Calendar.HOUR_OF_DAY)) + ":" + java.lang.String.valueOf(
                    cal.get(Calendar.MINUTE)
                ) + ":" + java.lang.String.valueOf(cal.get(Calendar.SECOND))
            intent.putExtra("title", "activity_app")
            intent.putExtra("time", AlarmTimeTag)
            // PendingIntent.FLAG_IMMUTABLE 要在 API 23 以上才能使用
            val pi =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    PendingIntent.getBroadcast(
                        context,
                        1,
                        intent,
                        PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                    )
                } else {
                    PendingIntent.getBroadcast(
                        context,
                        1,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                    )
                }
            val am = context.getSystemService(ALARM_SERVICE) as AlarmManager
            am.setExact(AlarmManager.RTC_WAKEUP, cal.timeInMillis, pi)
        }
    }
}

@Composable
fun Greeting(
    name: String,
    onButtonClick: () -> Unit
) {
    Row() {
        Text(text = "Hello $name!")
        Button(
            modifier = Modifier
                .padding(20.dp),
            onClick = {
                onButtonClick()
            }
        ) {
            Text(text = "APP")
        }
    }
}