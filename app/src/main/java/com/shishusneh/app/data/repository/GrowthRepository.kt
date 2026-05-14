package com.shishusneh.app.data.repository

import com.shishusneh.app.data.db.dao.GrowthRecordDao
import com.shishusneh.app.data.db.entities.GrowthRecord
import kotlinx.coroutines.flow.Flow

class GrowthRepository(private val dao: GrowthRecordDao) {

    fun getRecords(profileId: Int): Flow<List<GrowthRecord>> = dao.getRecordsByProfile(profileId)

    fun getLatestRecord(profileId: Int): Flow<GrowthRecord?> = dao.getLatestRecord(profileId)

    suspend fun insertRecord(record: GrowthRecord): Long = dao.insertRecord(record)

    suspend fun deleteRecord(record: GrowthRecord) = dao.deleteRecord(record)

    suspend fun getRecordsForChart(profileId: Int): List<GrowthRecord> =
        dao.getRecordsForChart(profileId)
}
