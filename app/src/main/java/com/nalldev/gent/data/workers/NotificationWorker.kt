package com.nalldev.gent.data.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.nalldev.gent.data.datasources.remote.ApiService
import com.nalldev.gent.utils.NotificationUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class NotificationWorker(
    context: Context,
    params: WorkerParameters,
) : CoroutineWorker(context, params), KoinComponent {

    private val apiService: ApiService by inject()

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getFirstActiveEvent()
            val event = response.listEvents?.firstOrNull()

            if (event != null) {
                NotificationUtils.showEventNotification(applicationContext, event)
            }

            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}