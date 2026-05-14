package com.shishusneh.app.worker

import android.content.Context
import androidx.work.*
import com.shishusneh.app.domain.VaccineSchedule
import java.util.concurrent.TimeUnit

object VaccineSchedulerHelper {

    fun scheduleAllVaccines(context: Context, babyDobMillis: Long) {
        val workManager = WorkManager.getInstance(context)
        val schedule = VaccineSchedule.getSchedule(babyDobMillis)
        val now = System.currentTimeMillis()

        // Cancel all existing vaccine reminders
        workManager.cancelAllWorkByTag("vaccine_reminder")

        schedule.forEach { item ->
            // Schedule reminder 2 days before and on the day
            val reminderTimes = listOf(
                item.dueDate - TimeUnit.DAYS.toMillis(2),  // 2 days before
                item.dueDate                                 // On the day
            )

            reminderTimes.forEachIndexed { index, triggerTime ->
                if (triggerTime > now) {
                    val delay = triggerTime - now
                    val daysUntilDue = TimeUnit.MILLISECONDS.toDays(item.dueDate - triggerTime)

                    val inputData = workDataOf(
                        VaccineReminderWorker.KEY_VACCINE_NAME to item.vaccine.name,
                        VaccineReminderWorker.KEY_DISEASE_PREVENTED to item.vaccine.diseasePrevented,
                        VaccineReminderWorker.KEY_DAYS_UNTIL to daysUntilDue,
                        VaccineReminderWorker.KEY_VACCINE_ID to "${item.vaccine.id}_$index"
                    )

                    val workRequest = OneTimeWorkRequestBuilder<VaccineReminderWorker>()
                        .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                        .setInputData(inputData)
                        .addTag("vaccine_reminder")
                        .addTag(item.vaccine.id)
                        .setConstraints(
                            Constraints.Builder()
                                .setRequiresBatteryNotLow(false)
                                .build()
                        )
                        .build()

                    workManager.enqueueUniqueWork(
                        "${item.vaccine.id}_reminder_$index",
                        ExistingWorkPolicy.REPLACE,
                        workRequest
                    )
                }
            }
        }
    }

    fun cancelAllReminders(context: Context) {
        WorkManager.getInstance(context).cancelAllWorkByTag("vaccine_reminder")
    }
}
