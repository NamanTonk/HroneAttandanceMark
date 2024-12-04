package com.tracker.hroneattandacncemark.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.tracker.hroneattandacncemark.notification.NotificationHelper

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, p1: Intent) {
        NotificationHelper(context).showNotification("Attendance","Mark the attendance")
    }
}