package com.shishusneh.app.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "growth_records")
data class GrowthRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val profileId: Int,
    val weightKg: Float,
    val heightCm: Float,
    val recordedAt: Long = System.currentTimeMillis(), // Unix timestamp in millis
    val notes: String = ""
)
