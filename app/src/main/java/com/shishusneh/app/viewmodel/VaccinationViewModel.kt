package com.shishusneh.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.shishusneh.app.ShishuSnehApplication
import com.shishusneh.app.data.db.entities.MotherProfile
import com.shishusneh.app.data.db.entities.VaccineDoneRecord
import com.shishusneh.app.data.repository.ProfileRepository
import com.shishusneh.app.domain.VaccineSchedule
import com.shishusneh.app.domain.VaccineScheduleItem
import com.shishusneh.app.domain.VaccineStatus
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class VaccinationUiState(
    val profile: MotherProfile? = null,
    val schedule: List<VaccineScheduleItem> = emptyList(),
    val doneCount: Int = 0,
    val totalCount: Int = 0,
    val dueSoonCount: Int = 0,
    val doneVaccineIds: Set<String> = emptySet(),
    val isLoading: Boolean = true
)

class VaccinationViewModel(application: Application) : AndroidViewModel(application) {

    private val database = (application as ShishuSnehApplication).database
    private val profileRepository = ProfileRepository(database.motherProfileDao())
    private val vaccineDoneDao = database.vaccineDoneDao()

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<VaccinationUiState> = profileRepository.activeProfile
        .flatMapLatest { profile ->
            if (profile == null) {
                flowOf(VaccinationUiState(isLoading = false))
            } else {
                vaccineDoneDao.getDoneRecords(profile.id).map { doneRecords ->
                    val doneIds = doneRecords.map { it.vaccineId }.toSet()
                    val baseSchedule = VaccineSchedule.getSchedule(profile.babyDob)
                    
                    val finalSchedule = baseSchedule.map { item ->
                        if (doneIds.contains(item.vaccine.id)) {
                            item.copy(status = VaccineStatus.DONE)
                        } else {
                            item
                        }
                    }

                    VaccinationUiState(
                        profile = profile,
                        schedule = finalSchedule,
                        doneCount = finalSchedule.count { it.status == VaccineStatus.DONE },
                        totalCount = finalSchedule.size,
                        dueSoonCount = finalSchedule.count { it.status == VaccineStatus.DUE_SOON },
                        doneVaccineIds = doneIds,
                        isLoading = false
                    )
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = VaccinationUiState(isLoading = true)
        )

    fun toggleDone(vaccineId: String, profileId: Int, currentlyDone: Boolean) {
        viewModelScope.launch {
            if (currentlyDone) {
                vaccineDoneDao.markUndone(vaccineId, profileId)
            } else {
                vaccineDoneDao.markDone(
                    VaccineDoneRecord(
                        vaccineId = vaccineId,
                        profileId = profileId
                    )
                )
            }
        }
    }
}
