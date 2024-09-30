package com.nalldev.gent.ui.fragment.about

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.nalldev.gent.data.workers.NotificationWorker
import com.nalldev.gent.domain.repositories.EventRepository
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class AboutViewModel(
    private val eventRepository: EventRepository,
    private val workManager: WorkManager
) : ViewModel() {
    val isDarkMode: LiveData<Boolean> = eventRepository.getIsDarkMode().asLiveData()
    val isNotificationEnabled: LiveData<Boolean> = eventRepository.getIsNotificationEnabled().asLiveData()

    fun setIsDarkMode(isDarkMode: Boolean) = viewModelScope.launch {
        eventRepository.setIsDarkMode(isDarkMode)
    }

    fun setNotificationEnabled(enabled: Boolean) {
        viewModelScope.launch {
            eventRepository.setIsNotificationEnabled(enabled)
            if (enabled) {
                scheduleNotificationWorker()
            } else {
                cancelNotificationWorker()
            }
        }
    }

    private fun scheduleNotificationWorker() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val request = PeriodicWorkRequestBuilder<NotificationWorker>(1, TimeUnit.DAYS)
            .setConstraints(constraints)
            .build()

        workManager.enqueueUniquePeriodicWork(
            "EventNotificationWork",
            ExistingPeriodicWorkPolicy.UPDATE,
            request
        )
    }

    private fun cancelNotificationWorker() {
        workManager.cancelUniqueWork("EventNotificationWork")
    }
}