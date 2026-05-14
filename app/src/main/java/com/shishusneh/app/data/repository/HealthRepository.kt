package com.shishusneh.app.data.repository

import com.shishusneh.app.data.db.dao.DiaperDao
import com.shishusneh.app.data.db.dao.FeedingDao
import com.shishusneh.app.data.db.dao.SleepDao
import com.shishusneh.app.data.db.dao.SymptomDao
import com.shishusneh.app.data.db.entities.DiaperRecord
import com.shishusneh.app.data.db.entities.FeedingRecord
import com.shishusneh.app.data.db.entities.SleepRecord
import com.shishusneh.app.data.db.entities.SymptomRecord
import kotlinx.coroutines.flow.Flow

class HealthRepository(
    private val feedingDao: FeedingDao,
    private val sleepDao: SleepDao,
    private val symptomDao: SymptomDao,
    private val diaperDao: DiaperDao
) {
    suspend fun logFeeding(record: FeedingRecord) {
        feedingDao.insertFeeding(record)
    }

    suspend fun getLastFeeding(profileId: Int): FeedingRecord? {
        return feedingDao.getLastFeeding(profileId)
    }

    suspend fun logSleepStart(profileId: Int, notes: String?) {
        val active = sleepDao.getCurrentActiveSleep(profileId)
        if (active == null) {
            sleepDao.insertSleep(SleepRecord(profileId = profileId, startTime = System.currentTimeMillis(), endTime = null, notes = notes))
        }
    }

    suspend fun logSleepEnd(profileId: Int, notes: String?) {
        val active = sleepDao.getCurrentActiveSleep(profileId)
        if (active != null) {
            val updated = active.copy(endTime = System.currentTimeMillis(), notes = notes ?: active.notes)
            sleepDao.updateSleep(updated)
        } else {
            // No active sleep, just create a new one ending now, starting 1 hr ago as a fallback
            val now = System.currentTimeMillis()
            sleepDao.insertSleep(SleepRecord(profileId = profileId, startTime = now - 3600000, endTime = now, notes = notes))
        }
    }

    suspend fun getLastSleep(profileId: Int): SleepRecord? {
        return sleepDao.getLastSleep(profileId)
    }

    suspend fun logSymptom(record: SymptomRecord) {
        symptomDao.insertSymptom(record)
    }

    suspend fun logDiaper(record: DiaperRecord) {
        diaperDao.insertDiaper(record)
    }

    fun getRecentFeedings(profileId: Int, limit: Int): Flow<List<FeedingRecord>> =
        feedingDao.getRecentFeedings(profileId, limit)

    fun getSleepSince(profileId: Int, sinceTime: Long): Flow<List<SleepRecord>> =
        sleepDao.getSleepSince(profileId, sinceTime)

    fun getRecentSymptoms(profileId: Int, limit: Int): Flow<List<SymptomRecord>> =
        symptomDao.getRecentSymptoms(profileId, limit)
        
    fun getRecentDiapers(profileId: Int, limit: Int): Flow<List<DiaperRecord>> =
        diaperDao.getRecentDiapers(profileId, limit)
}
