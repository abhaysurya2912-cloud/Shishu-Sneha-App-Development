package com.shishusneh.app.worker

import android.content.Context
import androidx.work.*
import java.util.concurrent.TimeUnit

class ReminderHelper(private val context: Context) {

    fun setReminder(title: String, message: String, delayMillis: Long): String {
        val inputData = Data.Builder()
            .putString(ReminderWorker.KEY_TITLE, title)
            .putString(ReminderWorker.KEY_MESSAGE, message)
            .build()

        val workRequest = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInitialDelay(delayMillis, TimeUnit.MILLISECONDS)
            .setInputData(inputData)
            .build()

        WorkManager.getInstance(context).enqueue(workRequest)

        return workRequest.id.toString()
    }

    fun cancelReminder(reminderId: String) {
        try {
            val uuid = java.util.UUID.fromString(reminderId)
            WorkManager.getInstance(context).cancelWorkById(uuid)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
