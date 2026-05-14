package com.shishusneh.app.data.db.dao

import androidx.room.*
import com.shishusneh.app.data.db.entities.VaccineDoneRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface VaccineDoneDao {
    @Query("SELECT * FROM vaccine_done_records WHERE profileId = :profileId")
    fun getDoneRecords(profileId: Int): Flow<List<VaccineDoneRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun markDone(record: VaccineDoneRecord)

    @Query("DELETE FROM vaccine_done_records WHERE vaccineId = :vaccineId AND profileId = :profileId")
    suspend fun markUndone(vaccineId: String, profileId: Int)
}
