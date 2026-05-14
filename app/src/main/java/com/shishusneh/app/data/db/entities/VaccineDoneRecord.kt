package com.shishusneh.app.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vaccine_done_records")
data class VaccineDoneRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val vaccineId: String,
    val profileId: Int,
    val markedDoneAt: Long = System.currentTimeMillis()
)
