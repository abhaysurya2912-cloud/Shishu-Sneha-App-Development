package com.shishusneh.app.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "milestone_records")
data class MilestoneRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val profileId: Int,
    val milestoneId: String,  // Unique ID like "week4_head_control"
    val isAchieved: Boolean = false,
    val achievedAt: Long? = null,
    val updatedAt: Long = System.currentTimeMillis()
)
