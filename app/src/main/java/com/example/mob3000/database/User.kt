package com.example.mob3000.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey val id: Int = 0,
    val username: String,
    val defaultAffirmationsHomeEnabled: Boolean = true,
    val defaultAffirmationsNotifEnabled: Boolean = true,
    val notificationIntervalHours: Int = 24
)