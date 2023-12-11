package com.udacity.asteroidradar.work

import android.app.Application
import android.content.Context
import android.os.Build
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.data.sourceoftruth.AsteroidsDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit

// ... (imports)

class AsteroidApplication : Application() {

    val applicationScope = CoroutineScope(Dispatchers.Default)

    private fun delayedInit() {
        applicationScope.launch {
            setupRecurringWork()
        }
    }

    private fun setupRecurringWork() {
        // Schedule worker to refresh asteroid data
        val refreshConstraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .setRequiresBatteryNotLow(true)
            .setRequiresCharging(true)
            .apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    setRequiresDeviceIdle(true)
                }
            }.build()

        val refreshRequest =
            PeriodicWorkRequestBuilder<RefreshAsteroidDataWorker>(1, TimeUnit.DAYS)
                .setConstraints(refreshConstraints)
                .build()

        WorkManager.getInstance().enqueueUniquePeriodicWork(
            RefreshAsteroidDataWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            refreshRequest
        )

        // Schedule worker to delete previous day asteroids
        val deleteConstraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .setRequiresBatteryNotLow(true)
            .setRequiresCharging(true)
            .apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    setRequiresDeviceIdle(true)
                }
            }.build()

        val deleteRequest =
            OneTimeWorkRequestBuilder<DeletePreviousDayAsteroidsWorker>()
                .setConstraints(deleteConstraints)
                .build()

        WorkManager.getInstance().enqueueUniqueWork(
            DeletePreviousDayAsteroidsWorker.WORK_NAME,
            ExistingWorkPolicy.KEEP,
            deleteRequest
        )
    }

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        delayedInit()
    }
}

class DeletePreviousDayAsteroidsWorker(
    appContext: Context,
    params: WorkerParameters,
    private val database: AsteroidsDatabase
) :
    CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = "DeletePreviousDayAsteroidsWorker"
    }

    override suspend fun doWork(): Result = coroutineScope {
        try {
            Timber.d("Deleting previous day asteroids...")

            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_YEAR, -1)
            val yesterday = calendar.time

            val formattedDate =
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(yesterday)

            // Delete asteroids from the previous day in the database
            database.asteroidsDao.deletePreviousDayAsteroids(formattedDate)

            Timber.d("Asteroids from $formattedDate deleted successfully.")
            Result.success()
        } catch (e: Exception) {
            Timber.e(e, "Error deleting previous day asteroids")
            Result.failure()
        }
    }
}
