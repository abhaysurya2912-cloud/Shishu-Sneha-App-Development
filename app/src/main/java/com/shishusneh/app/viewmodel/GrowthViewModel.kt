package com.shishusneh.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.shishusneh.app.ShishuSnehApplication
import com.shishusneh.app.data.db.entities.GrowthRecord
import com.shishusneh.app.data.repository.GrowthRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class GrowthViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = GrowthRepository(
        (application as ShishuSnehApplication).database.growthRecordDao()
    )

    private val _profileId = MutableStateFlow(0)

    val records: StateFlow<List<GrowthRecord>> = _profileId
        .flatMapLatest { id ->
            if (id > 0) repository.getRecords(id) else flowOf(emptyList())
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val latestRecord: StateFlow<GrowthRecord?> = _profileId
        .flatMapLatest { id ->
            if (id > 0) repository.getLatestRecord(id) else flowOf(null)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _chartData = MutableStateFlow<List<GrowthRecord>>(emptyList())
    val chartData: StateFlow<List<GrowthRecord>> = _chartData.asStateFlow()

    fun setProfileId(id: Int) {
        _profileId.value = id
        viewModelScope.launch {
            _chartData.value = repository.getRecordsForChart(id)
        }
    }

    fun addRecord(profileId: Int, weightKg: Float, heightCm: Float, notes: String = "") {
        viewModelScope.launch {
            val record = GrowthRecord(
                profileId = profileId,
                weightKg = weightKg,
                heightCm = heightCm,
                notes = notes
            )
            repository.insertRecord(record)
            _chartData.value = repository.getRecordsForChart(profileId)
        }
    }

    fun deleteRecord(record: GrowthRecord) {
        viewModelScope.launch {
            repository.deleteRecord(record)
        }
    }
}
