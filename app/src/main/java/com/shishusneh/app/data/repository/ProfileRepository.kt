package com.shishusneh.app.data.repository

import com.shishusneh.app.data.db.dao.MotherProfileDao
import com.shishusneh.app.data.db.entities.MotherProfile
import kotlinx.coroutines.flow.Flow

class ProfileRepository(private val dao: MotherProfileDao) {

    val activeProfile: Flow<MotherProfile?> = dao.getActiveProfile()

    val allProfiles: Flow<List<MotherProfile>> = dao.getAllProfiles()

    suspend fun saveProfile(profile: MotherProfile): Long {
        val id = dao.insertProfile(profile)
        dao.setActiveProfile(id.toInt())
        return id
    }

    suspend fun setActiveProfile(id: Int) {
        dao.setActiveProfile(id)
    }

    suspend fun updateProfile(profile: MotherProfile) {
        dao.updateProfile(profile)
    }

    suspend fun hasProfile(): Boolean {
        return dao.getProfileCount() > 0
    }
}
