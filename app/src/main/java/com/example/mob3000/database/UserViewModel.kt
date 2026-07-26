package com.example.mob3000.database

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class UserViewModel(context: Context) : ViewModel() {

    private val dao = AppDatabase.getInstance(context).userDao()

    var user: User? by mutableStateOf(null)
        private set

    var showNameDialog by mutableStateOf(false)
        private set

    init {
        loadUser()
    }

    private fun loadUser() {
        viewModelScope.launch {
            val existing = dao.getUser()
            if (existing != null) {
                user = existing
                if (existing.username.isBlank()) {
                    showNameDialog = true
                }
            } else {
                val newUser = User(
                    id = 0,
                    username = "",
                    defaultAffirmationsHomeEnabled = true,
                    defaultAffirmationsNotifEnabled = true,
                    notificationIntervalHours = 24,
                )
                dao.insertUser(newUser)
                user = newUser
                showNameDialog = true
            }
        }
    }

    fun updateDefaultAffHome(enabled: Boolean) {
        viewModelScope.launch {
            val current = user ?: return@launch
            val updated = current.copy(defaultAffirmationsHomeEnabled = enabled)
            dao.insertUser(updated)
            user = updated
        }
    }

    fun updateDefaultAffNotif(enabled: Boolean) {
        viewModelScope.launch {
            val current = user ?: return@launch
            val updated = current.copy(defaultAffirmationsNotifEnabled = enabled)
            dao.insertUser(updated)
            user = updated
        }
    }

    fun updateNotificationInterval(hours: Int) {
        viewModelScope.launch {
            val current = user ?: return@launch
            val updated = current.copy(notificationIntervalHours = hours)
            dao.insertUser(updated)
            user = updated
        }
    }

    fun updateUsername(newName: String) {
        viewModelScope.launch {
            val current = user ?: return@launch
            val updated = current.copy(username = newName)
            dao.insertUser(updated)
            user = updated
            showNameDialog = false
        }
    }
}
