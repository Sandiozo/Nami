package com.example.mob3000.database

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class AffirmationViewModel(context: Context) : ViewModel() {

    private val dao = AppDatabase.getInstance(context).affirmationDao()

    var customAffirmations: List<CustomAff> by mutableStateOf(emptyList())
        private set

    var randomAffirmation: String? by mutableStateOf(null)
        private set

    init {
        loadCustomAffirmations()
        refreshRandomAffirmation()
    }

    private fun loadCustomAffirmations() {
        viewModelScope.launch {
            customAffirmations = dao.getCustomAffirmations()
        }
    }

    fun refreshRandomAffirmation() {
        viewModelScope.launch {
            val defaults = dao.getDefaultAffirmations()
            val customs = dao.getCustomAffirmations()

            val allTexts = defaults.map { it.text } + customs.map { it.text }

            randomAffirmation = allTexts.randomOrNull()
        }
    }


    fun addCustomAff(text: String) {
        viewModelScope.launch {
            dao.insertCustomAff(CustomAff(text = text))
            customAffirmations = dao.getCustomAffirmations()
            refreshRandomAffirmation()
        }
    }

    fun deleteCustomAff(aff: CustomAff) {
        viewModelScope.launch {
            dao.deleteCustomAff(aff)
            customAffirmations = dao.getCustomAffirmations()
            refreshRandomAffirmation()
        }
    }
}
