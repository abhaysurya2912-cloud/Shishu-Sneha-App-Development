package com.shishusneh.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.shishusneh.app.ShishuSnehApplication
import com.shishusneh.app.data.repository.MilestoneRepository
import com.shishusneh.app.data.db.entities.MilestoneRecord
import com.shishusneh.app.domain.MilestoneData
import com.shishusneh.app.domain.MilestoneItem
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class MilestoneUiItem(
    val milestone: MilestoneItem,
    val record: MilestoneRecord?
)

class MilestoneViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = MilestoneRepository(
        (application as ShishuSnehApplication).database.milestoneDao()
    )

    private val _profileId = MutableStateFlow(0)
    private val _babyAgeWeeks = MutableStateFlow(0)

    val dbRecords: StateFlow<List<MilestoneRecord>> = _profileId
        .flatMapLatest { id ->
            if (id > 0) repository.getMilestones(id) else flowOf(emptyList())
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val milestoneUiItems: StateFlow<Map<Int, List<MilestoneUiItem>>> = combine(
        _babyAgeWeeks, dbRecords
    ) { ageWeeks, records ->
        val groups = MilestoneData.getMilestoneGroups(ageWeeks)
        groups.mapValues { (_, milestones) ->
            milestones.map { milestone ->
                MilestoneUiItem(
                    milestone = milestone,
                    record = records.find { it.milestoneId == milestone.id }
                )
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())

    val achievedCount: StateFlow<Int> = dbRecords.map { records ->
        records.count { it.isAchieved }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    fun setProfile(profileId: Int, ageWeeks: Int) {
        _profileId.value = profileId
        _babyAgeWeeks.value = ageWeeks
    }

    fun updateMilestone(milestoneId: String, achieved: Boolean) {
        val profileId = _profileId.value
        if (profileId <= 0) return
        viewModelScope.launch {
            repository.updateMilestoneStatus(profileId, milestoneId, achieved)
        }
    }
}
