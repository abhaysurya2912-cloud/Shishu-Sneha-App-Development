package com.shishusneh.app.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mother_profile")
data class MotherProfile(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val motherName: String,
    val babyName: String,
    val babyGender: String, // "Boy" or "Girl"
    val babyDob: Long, // Unix timestamp in millis
    val isActive: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
