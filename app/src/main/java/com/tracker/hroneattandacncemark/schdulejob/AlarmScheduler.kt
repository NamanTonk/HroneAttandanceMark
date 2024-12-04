package com.tracker.hroneattandacncemark.schdulejob

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
import com.tracker.hroneattandacncemark.receiver.AlarmReceiver
import java.util.Calendar

class AlarmScheduler(private val context: Context,private val horus:Int,private val min:Int) {

    fun scheduleWeekdayAlarm() {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Set up the calendar to 10:00 AM for the next weekday
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, horus)
            set(Calendar.MINUTE, min)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)

            // If today is not a weekday or the time has passed, move to the next weekday
            if (!isWeekday() || timeInMillis <= System.currentTimeMillis()) {
                moveToNextWeekday()
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmManager.canScheduleExactAlarms())
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
            else context.startActivity(Intent(ACTION_REQUEST_SCHEDULE_EXACT_ALARM))
        } else {
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY, // Set to trigger daily
                pendingIntent
            )
        }
    }

    // only WeekDays
    private fun Calendar.isWeekday(): Boolean {
        val dayOfWeek = get(Calendar.DAY_OF_WEEK)
        return dayOfWeek in Calendar.MONDAY..Calendar.FRIDAY
    }

    private fun Calendar.moveToNextWeekday() {
        do {
            add(Calendar.DAY_OF_MONTH, 1)
        } while (!isWeekday())
    }
}