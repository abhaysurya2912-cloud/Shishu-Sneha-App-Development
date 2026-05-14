package com.shishusneh.app.data.db.dao

import androidx.room.*
import com.shishusneh.app.data.db.entities.MotherProfile
import kotlinx.coroutines.flow.Flow

@Dao
interface MotherProfileDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: MotherProfile): Long

    @Update
    suspend fun updateProfile(profile: MotherProfile)

    @Delete
    suspend fun deleteProfile(profile: MotherProfile)

    @Query("SELECT * FROM mother_profile ORDER BY createdAt DESC")
    fun getAllProfiles(): Flow<List<MotherProfile>>

    @Query("SELECT * FROM mother_profile WHERE isActive = 1 LIMIT 1")
    fun getActiveProfile(): Flow<MotherProfile?>

    @Query("UPDATE mother_profile SET isActive = 0")
    suspend fun deactivateAll()

    @Query("UPDATE mother_profile SET isActive = 1 WHERE id = :id")
    suspend fun activateProfileById(id: Int)

    @Transaction
    suspend fun setActiveProfile(id: Int) {
        deactivateAll()
        activateProfileById(id)
    }

    @Query("SELECT * FROM mother_profile WHERE id = :id")
    suspend fun getProfileById(id: Int): MotherProfile?

    @Query("SELECT COUNT(*) FROM mother_profile")
    suspend fun getProfileCount(): Int
}
