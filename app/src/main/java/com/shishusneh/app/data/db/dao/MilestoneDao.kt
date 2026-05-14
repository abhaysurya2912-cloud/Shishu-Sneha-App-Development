package com.shishusneh.app.data.db.dao

import androidx.room.*
import com.shishusneh.app.data.db.entities.MilestoneRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface MilestoneDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMilestone(record: MilestoneRecord): Long

    @Update
    suspend fun updateMilestone(record: MilestoneRecord)

    @Query("SELECT * FROM milestone_records WHERE profileId = :profileId")
    fun getMilestonesByProfile(profileId: Int): Flow<List<MilestoneRecord>>

    @Query("SELECT * FROM milestone_records WHERE profileId = :profileId AND milestoneId = :milestoneId")
    suspend fun getMilestoneById(profileId: Int, milestoneId: String): MilestoneRecord?

    @Query("SELECT COUNT(*) FROM milestone_records WHERE profileId = :profileId AND isAchieved = 1")
    fun getAchievedCount(profileId: Int): Flow<Int>

    @Query("""
        UPDATE milestone_records 
        SET isAchieved = :achieved, achievedAt = :achievedAt, updatedAt = :updatedAt
        WHERE profileId = :profileId AND milestoneId = :milestoneId
    """)
    suspend fun updateMilestoneStatus(
        profileId: Int,
        milestoneId: String,
        achieved: Boolean,
        achievedAt: Long?,
        updatedAt: Long = System.currentTimeMillis()
    )
}
