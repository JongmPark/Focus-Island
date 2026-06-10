package com.example.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.Routine
import com.example.data.RoutineDatabase
import com.example.data.RoutineRepository
import com.example.widget.RoutineWidgetProvider
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RoutineViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: RoutineRepository
    private val sharedPrefs = application.getSharedPreferences("focus_island_prefs", Context.MODE_PRIVATE)

    // Reactive flow of routines from database
    val allRoutines: StateFlow<List<Routine>>

    // Subscription status (persisted locally)
    private val _isPremium = MutableStateFlow(sharedPrefs.getBoolean("is_premium", false))
    val isPremium: StateFlow<Boolean> = _isPremium.asStateFlow()

    // Tracking for AdMob interstitial popups (triggered once, e.g. when all routines are completed)
    private val _interstitialTriggerState = MutableStateFlow(false)
    val interstitialTriggerState: StateFlow<Boolean> = _interstitialTriggerState.asStateFlow()

    init {
        val database = RoutineDatabase.getDatabase(application)
        repository = RoutineRepository(database.routineDao())

        allRoutines = repository.allRoutinesFlow.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        checkAndResetDailyRoutines()
    }

    private fun getTodayDateString(): String {
         val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
         return sdf.format(Date())
    }

    private fun checkAndResetDailyRoutines() {
        viewModelScope.launch {
            val today = getTodayDateString()
            try {
                val list = repository.getAllRoutinesList()
                // If any routine last updated date is different from today, trigger zero reset
                val needsReset = list.any { it.lastUpdatedDate != today }
                if (needsReset) {
                    repository.resetAllCompletionStates(today)
                }
                updateWidget()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun addRoutine(title: String, colorHex: String) {
        viewModelScope.launch {
            val today = getTodayDateString()
            val currentList = repository.getAllRoutinesList()
            val order = currentList.size
            val newRoutine = Routine(
                title = title,
                colorHex = colorHex,
                isCompleted = false,
                displayOrder = order,
                lastUpdatedDate = today
            )
            repository.insert(newRoutine)
            updateWidget()
        }
    }

    fun toggleRoutineCompletion(routine: Routine, onAllDone: () -> Unit) {
        viewModelScope.launch {
            val updated = routine.copy(isCompleted = !routine.isCompleted, lastUpdatedDate = getTodayDateString())
            repository.update(updated)
            updateWidget()

            // Check if all routines are now completed
            val listBeforeSync = repository.getAllRoutinesList()
            // Map index of matching item to the updated state
            val list = listBeforeSync.map { if (it.id == routine.id) updated else it }
            
            if (list.isNotEmpty() && list.all { it.isCompleted }) {
                // Trigger interstitial ad popup (simulated or displayed based on premium status)
                if (!_isPremium.value) {
                    _interstitialTriggerState.value = true
                }
                onAllDone()
            }
        }
    }

    fun dismissInterstitial() {
        _interstitialTriggerState.value = false
    }

    fun deleteRoutine(routine: Routine) {
        viewModelScope.launch {
            repository.delete(routine)
            updateWidget()
        }
    }

    fun togglePremium() {
        val next = !_isPremium.value
        _isPremium.value = next
        sharedPrefs.edit().putBoolean("is_premium", next).apply()
        updateWidget()
    }

    private fun updateWidget() {
        RoutineWidgetProvider.triggerUpdate(getApplication())
    }
}
