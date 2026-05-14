package com.shishusneh.app.worker

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.shishusneh.app.MainActivity
import com.shishusneh.app.R
import com.shishusneh.app.ShishuSnehApplication

class VaccineReminderWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val vaccineName = inputData.getString(KEY_VACCINE_NAME) ?: return Result.failure()
        val diseasePrevented = inputData.getString(KEY_DISEASE_PREVENTED) ?: ""
        val daysUntil = inputData.getLong(KEY_DAYS_UNTIL, 0L)
        val vaccineId = inputData.getString(KEY_VACCINE_ID) ?: vaccineName

        val message = when {
            daysUntil <= 0 -> "⚠️ ${vaccineName} is due today! It protects against $diseasePrevented."
            daysUntil == 1L -> "Tomorrow's vaccine: $vaccineName prevents $diseasePrevented"
            else -> "Upcoming in $daysUntil days: $vaccineName — protects against $diseasePrevented"
        }

        showNotification(
            title = "💉 Vaccine Reminder: $vaccineName",
            message = message,
            notificationId = vaccineId.hashCode()
        )

        return Result.success()
    }

    private fun showNotification(title: String, message: String, notificationId: Int) {
        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("navigate_to", "vaccination")
        }
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            notificationId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(
            applicationContext,
            ShishuSnehApplication.VACCINE_CHANNEL_ID
        )
            .setSmallIcon(android.R.drawable.ic_popup_reminder)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setColor(0xFFFF8A65.toInt())
            .build()

        val manager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager
        manager.notify(notificationId, notification)
    }

    companion object {
        const val KEY_VACCINE_NAME = "vaccine_name"
        const val KEY_DISEASE_PREVENTED = "disease_prevented"
        const val KEY_DAYS_UNTIL = "days_until"
        const val KEY_VACCINE_ID = "vaccine_id"
    }
}
