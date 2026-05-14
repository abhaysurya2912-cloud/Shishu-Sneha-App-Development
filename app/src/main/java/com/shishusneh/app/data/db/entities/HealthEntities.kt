package com.shishusneh.app.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "feeding_records")
data class FeedingRecord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val profileId: Int, // The baby's ID
    val type: String, // "breast", "bottle", "solid"
    val side: String?, // "left", "right", "both", null if bottle
    val durationMinutes: Int?, // if breast
    val amountMl: Int?, // if bottle
    val notes: String?,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "sleep_records")
data class SleepRecord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val profileId: Int,
    val startTime: Long,
    val endTime: Long?, // null if still sleeping
    val notes: String?
)

@Entity(tableName = "symptom_records")
data class SymptomRecord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val profileId: Int,
    val symptom: String, // e.g. "fever", "rash"
    val severity: String, // "mild", "moderate", "severe"
    val notes: String?,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "diaper_records")
data class DiaperRecord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val profileId: Int,
    val type: String, // "wet", "dirty", "both"
    val notes: String?,
    val timestamp: Long = System.currentTimeMillis()
)
