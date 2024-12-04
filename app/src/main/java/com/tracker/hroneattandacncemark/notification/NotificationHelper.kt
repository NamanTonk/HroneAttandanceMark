package com.tracker.hroneattandacncemark.notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.tracker.hroneattandacncemark.receiver.NotificationClickReceiver

class NotificationHelper(private val context: Context) {

    private val CHANNEL_ID = "hr_one_mark_attendance_channel_id"
    private val NOTIFICATION_ID = 1

    companion object {
        const val ACCEPT = "com.notification.click.Accept"
        const val REJECT = "com.notification.click.Reject"
    }

    fun closeNotification() {
        NotificationManagerCompat.from(context).cancel(NOTIFICATION_ID)
    }

    fun showNotification(title: String, message: String) {
        val actionMark =
            PendingIntent.getBroadcast(context, 0, Intent(ACCEPT), PendingIntent.FLAG_IMMUTABLE)
        val dismissMark =
            PendingIntent.getBroadcast(context, 0, Intent(REJECT), PendingIntent.FLAG_IMMUTABLE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Example Channel"
            val descriptionText = "This is an example notification channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(message)
            .addAction(android.R.drawable.ic_input_add, "Mark", actionMark)
            .addAction(android.R.drawable.ic_menu_close_clear_cancel, "Dismiss", dismissMark)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
        // Build the notification

        // Show the notification
        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) notify(NOTIFICATION_ID, builder.build())
        }
    }
}