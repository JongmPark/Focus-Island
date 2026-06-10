package com.example.data

import kotlinx.coroutines.flow.Flow

class RoutineRepository(private val routineDao: RoutineDao) {
    val allRoutinesFlow: Flow<List<Routine>> = routineDao.getAllRoutinesFlow()

    suspend fun getAllRoutinesList(): List<Routine> = routineDao.getAllRoutinesList()

    suspend fun insert(routine: Routine) {
        routineDao.insertRoutine(routine)
    }

    suspend fun update(routine: Routine) {
        routineDao.updateRoutine(routine)
    }

    suspend fun delete(routine: Routine) {
        routineDao.deleteRoutine(routine)
    }

    suspend fun resetAllCompletionStates(todayDate: String) {
        routineDao.resetAllCompletionStates(todayDate)
    }
}
