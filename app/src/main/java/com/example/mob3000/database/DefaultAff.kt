package com.example.mob3000.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "default_aff")
data class DefaultAff(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val text: String
)
