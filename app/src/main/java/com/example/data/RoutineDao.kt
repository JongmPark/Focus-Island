package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RoutineDao {
    @Query("SELECT * FROM routines ORDER BY displayOrder ASC, id ASC")
    fun getAllRoutinesFlow(): Flow<List<Routine>>

    @Query("SELECT * FROM routines ORDER BY displayOrder ASC, id ASC")
    suspend fun getAllRoutinesList(): List<Routine>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoutine(routine: Routine)

    @Update
    suspend fun updateRoutine(routine: Routine)

    @Delete
    suspend fun deleteRoutine(routine: Routine)

    @Query("UPDATE routines SET isCompleted = 0, lastUpdatedDate = :todayDate")
    suspend fun resetAllCompletionStates(todayDate: String)
}
