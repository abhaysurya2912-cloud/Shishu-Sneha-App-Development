package com.shishusneh.app.data.db.dao

import androidx.room.*
import com.shishusneh.app.data.db.entities.GrowthRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface GrowthRecordDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(record: GrowthRecord): Long

    @Update
    suspend fun updateRecord(record: GrowthRecord)

    @Delete
    suspend fun deleteRecord(record: GrowthRecord)

    @Query("SELECT * FROM growth_records WHERE profileId = :profileId ORDER BY recordedAt DESC")
    fun getRecordsByProfile(profileId: Int): Flow<List<GrowthRecord>>

    @Query("SELECT * FROM growth_records WHERE profileId = :profileId ORDER BY recordedAt DESC LIMIT 1")
    fun getLatestRecord(profileId: Int): Flow<GrowthRecord?>

    @Query("SELECT * FROM growth_records WHERE profileId = :profileId ORDER BY recordedAt ASC")
    suspend fun getRecordsForChart(profileId: Int): List<GrowthRecord>

    @Query("DELETE FROM growth_records WHERE id = :recordId")
    suspend fun deleteById(recordId: Int)
}
