package com.example.mob3000.notifications

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.mob3000.database.AppDatabase

class AffirmationWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val db = AppDatabase.getInstance(applicationContext)
        val affirmationDao = db.affirmationDao()
        val userDao = db.userDao()
        val user = userDao.getUser()
        val includeDefaults = user?.defaultAffirmationsNotifEnabled ?: true

        val defaultAffs = if (includeDefaults) {
            affirmationDao.getDefaultAffirmations()
        } else {
            emptyList()
        }

        val customAffs = affirmationDao.getCustomAffirmations()

        val all = (defaultAffs.map { it.text } + customAffs.map { it.text })

        val text = if (all.isNotEmpty()) {
            all.random()
        } else {
            "I am allowed to slow down and breathe."
        }

        NotificationHelper.showAffirmationNotification(applicationContext, text)

        return Result.success()
    }
}
