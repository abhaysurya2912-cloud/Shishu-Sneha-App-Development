package com.shishusneh.app.data.repository

import com.shishusneh.app.data.db.dao.MilestoneDao
import com.shishusneh.app.data.db.entities.MilestoneRecord
import kotlinx.coroutines.flow.Flow

class MilestoneRepository(private val dao: MilestoneDao) {

    fun getMilestones(profileId: Int): Flow<List<MilestoneRecord>> =
        dao.getMilestonesByProfile(profileId)

    fun getAchievedCount(profileId: Int): Flow<Int> = dao.getAchievedCount(profileId)

    suspend fun saveMilestone(record: MilestoneRecord): Long = dao.insertMilestone(record)

    suspend fun updateMilestoneStatus(
        profileId: Int,
        milestoneId: String,
        achieved: Boolean
    ) {
        val existing = dao.getMilestoneById(profileId, milestoneId)
        if (existing != null) {
            dao.updateMilestoneStatus(
                profileId = profileId,
                milestoneId = milestoneId,
                achieved = achieved,
                achievedAt = if (achieved) System.currentTimeMillis() else null
            )
        } else {
            dao.insertMilestone(
                MilestoneRecord(
                    profileId = profileId,
                    milestoneId = milestoneId,
                    isAchieved = achieved,
                    achievedAt = if (achieved) System.currentTimeMillis() else null
                )
            )
        }
    }
}
