package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "routines")
data class Routine(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val isCompleted: Boolean = false,
    val displayOrder: Int = 0,
    val colorHex: String = "#F5F5F7",
    val lastUpdatedDate: String = "" // formatted "yyyy-MM-dd" to check daily resets
)
