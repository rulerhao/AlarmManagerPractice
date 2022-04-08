package com.rulhouse.alarmmanagerpractice

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import java.util.*

class BootUpReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action.equals("android.intent.action.BOOT_COMPLETED")) {
            /* 收到廣播後要做的事 */

            //建立通知發布鬧鐘
            val cal: Calendar = GregorianCalendar(TimeZone.getTimeZone("GMT+8:00")) //取得時間
            for (i in 0..9) {
                cal.add(Calendar.MINUTE, 1) //加一分鐘
                cal.set(Calendar.SECOND, 0) //設定秒數為0
                MainActivity.add_alarm(context, cal) //註冊鬧鐘
            }
        }
    }
}