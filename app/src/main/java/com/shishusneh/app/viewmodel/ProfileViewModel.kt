package com.shishusneh.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.shishusneh.app.ShishuSnehApplication
import com.shishusneh.app.data.db.entities.MotherProfile
import com.shishusneh.app.data.repository.ProfileRepository
import com.shishusneh.app.worker.VaccineSchedulerHelper
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ProfileRepository(
        (application as ShishuSnehApplication).database.motherProfileDao()
    )

    val activeProfile: StateFlow<MotherProfile?> = repository.activeProfile.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    val allProfiles: StateFlow<List<MotherProfile>> = repository.allProfiles.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun saveProfile(
        motherName: String,
        babyName: String,
        babyGender: String,
        babyDob: Long
    ) {
        viewModelScope.launch {
            val profile = MotherProfile(
                motherName = motherName,
                babyName = babyName,
                babyGender = babyGender,
                babyDob = babyDob,
                isActive = true
            )
            repository.saveProfile(profile)
            // Schedule vaccine reminders based on DOB
            VaccineSchedulerHelper.scheduleAllVaccines(getApplication(), babyDob)
        }
    }

    fun setActiveProfile(id: Int) {
        viewModelScope.launch {
            repository.setActiveProfile(id)
        }
    }

    suspend fun hasProfile(): Boolean = repository.hasProfile()
}
