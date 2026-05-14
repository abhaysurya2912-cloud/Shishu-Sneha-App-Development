package com.shishusneh.app.worker

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            // Re-schedule vaccines after phone restart
            CoroutineScope(Dispatchers.IO).launch {
                val db = com.shishusneh.app.data.db.AppDatabase.getDatabase(context)
                val profile = db.motherProfileDao().getActiveProfile()
                // Note: in production use proper coroutine collection
                // This is a simplified boot receiver
            }
        }
    }
}
