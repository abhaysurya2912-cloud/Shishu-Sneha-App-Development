package com.shishusneh.app.data.db.dao

import androidx.room.*
import com.shishusneh.app.data.db.entities.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FeedingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFeeding(record: FeedingRecord)

    @Query("SELECT * FROM feeding_records WHERE profileId = :profileId ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentFeedings(profileId: Int, limit: Int): Flow<List<FeedingRecord>>

    @Query("SELECT * FROM feeding_records WHERE profileId = :profileId ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLastFeeding(profileId: Int): FeedingRecord?
}

@Dao
interface SleepDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSleep(record: SleepRecord): Long

    @Update
    suspend fun updateSleep(record: SleepRecord)

    @Query("SELECT * FROM sleep_records WHERE profileId = :profileId AND endTime IS NULL ORDER BY startTime DESC LIMIT 1")
    suspend fun getCurrentActiveSleep(profileId: Int): SleepRecord?

    @Query("SELECT * FROM sleep_records WHERE profileId = :profileId ORDER BY startTime DESC LIMIT 1")
    suspend fun getLastSleep(profileId: Int): SleepRecord?

    @Query("SELECT * FROM sleep_records WHERE profileId = :profileId AND startTime >= :sinceTime ORDER BY startTime DESC")
    fun getSleepSince(profileId: Int, sinceTime: Long): Flow<List<SleepRecord>>
}

@Dao
interface SymptomDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSymptom(record: SymptomRecord)

    @Query("SELECT * FROM symptom_records WHERE profileId = :profileId ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentSymptoms(profileId: Int, limit: Int): Flow<List<SymptomRecord>>
}

@Dao
interface DiaperDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDiaper(record: DiaperRecord)

    @Query("SELECT * FROM diaper_records WHERE profileId = :profileId ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentDiapers(profileId: Int, limit: Int): Flow<List<DiaperRecord>>
}
