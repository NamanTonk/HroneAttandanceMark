package com.tracker.hroneattandacncemark.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.WorkManager
import com.tracker.hroneattandacncemark.attandenceworker.AttandenceWorker
import com.tracker.hroneattandacncemark.notification.NotificationHelper

class NotificationClickReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, p1: Intent) {
        if (p1.action == NotificationHelper.ACCEPT)
            WorkManager.getInstance(context).enqueue(AttandenceWorker.getRequest())
        NotificationHelper(context = context).closeNotification()
    }
}