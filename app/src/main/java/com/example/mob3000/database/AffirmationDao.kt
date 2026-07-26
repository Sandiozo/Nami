package com.example.mob3000.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Delete

@Dao
interface AffirmationDao {

    // DEFAULT AFFIRMATIONS
    @Query("SELECT * FROM default_aff")
    suspend fun getDefaultAffirmations(): List<DefaultAff>

    @Insert
    suspend fun insertDefaultAff(aff: DefaultAff)

    @Query("DELETE FROM default_aff")
    suspend fun clearDefaultAff()

    // CUSTOM AFFIRMATIONS
    @Query("SELECT * FROM custom_aff")
    suspend fun getCustomAffirmations(): List<CustomAff>

    @Insert
    suspend fun insertCustomAff(aff: CustomAff)

    @Delete
    suspend fun deleteCustomAff(aff: CustomAff)

    @Query("DELETE FROM custom_aff")
    suspend fun clearCustomAff()
}
