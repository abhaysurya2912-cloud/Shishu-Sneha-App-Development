package com.shishusneh.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.shishusneh.app.ShishuSnehApplication
import com.shishusneh.app.data.db.entities.GrowthRecord
import com.shishusneh.app.data.db.entities.MotherProfile
import com.shishusneh.app.data.repository.GrowthRepository
import com.shishusneh.app.data.repository.ProfileRepository
import com.shishusneh.app.domain.VaccineSchedule
import com.shishusneh.app.domain.VaccineScheduleItem
import kotlinx.coroutines.flow.*
import java.util.concurrent.TimeUnit

data class HomeUiState(
    val profile: MotherProfile? = null,
    val babyAgeWeeks: Int = 0,
    val babyAgeDays: Int = 0,
    val latestWeight: GrowthRecord? = null,
    val nextVaccine: VaccineScheduleItem? = null,
    val isLoading: Boolean = true
)

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val profileRepository = ProfileRepository(
        (application as ShishuSnehApplication).database.motherProfileDao()
    )
    private val growthRepository = GrowthRepository(
        (application as ShishuSnehApplication).database.growthRecordDao()
    )

    val uiState: StateFlow<HomeUiState> = profileRepository.activeProfile
        .flatMapLatest { profile ->
            if (profile == null) {
                flowOf(HomeUiState(isLoading = false))
            } else {
                growthRepository.getLatestRecord(profile.id).map { latestWeight ->
                    val now = System.currentTimeMillis()
                    val ageDays = TimeUnit.MILLISECONDS.toDays(now - profile.babyDob).toInt()
                    val ageWeeks = ageDays / 7
                    val nextVaccine = VaccineSchedule.getNextVaccine(profile.babyDob)

                    HomeUiState(
                        profile = profile,
                        babyAgeWeeks = ageWeeks,
                        babyAgeDays = ageDays,
                        latestWeight = latestWeight,
                        nextVaccine = nextVaccine,
                        isLoading = false
                    )
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HomeUiState(isLoading = true)
        )
}
