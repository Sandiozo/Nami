package com.example.mob3000.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "custom_aff")
data class CustomAff(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val text: String
)
