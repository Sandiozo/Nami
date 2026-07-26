package com.example.mob3000

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.core.app.ActivityCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.mob3000.database.AppDatabase
import com.example.mob3000.database.UserViewModel
import com.example.mob3000.notifications.AffirmationWorker
import com.example.mob3000.notifications.NotificationHelper
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Thread.sleep(3000)
        installSplashScreen()

        askNotificationPermission()
        NotificationHelper.createChannel(this)
        scheduleAffirmationWorker()

        setContent {
            val vm = remember { UserViewModel(applicationContext) }
            MainScreen(vm)
        }
    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1001
                )
            }
        }
    }

    private fun scheduleAffirmationWorker() {
        val db = AppDatabase.getInstance(this)
        val userDao = db.userDao()

        lifecycleScope.launch {
            val user = userDao.getUser()
            val hours = user?.notificationIntervalHours ?: 24

            val request = PeriodicWorkRequestBuilder<AffirmationWorker>(
                hours.toLong(),
                TimeUnit.HOURS
            ).build()

            WorkManager.getInstance(this@MainActivity)
                .enqueueUniquePeriodicWork(
                    "daily_affirmation",
                    ExistingPeriodicWorkPolicy.UPDATE,
                    request
                )
        }
    }
}
