package com.manikandareas.devent.ui.fragment.preferences

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.manikandareas.devent.data.workers.NotificationWorker
import com.manikandareas.devent.domain.repositories.EventRepository
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit


class PreferencesViewModel(
    private val eventRepository: EventRepository,
    private val workManager: WorkManager
) : ViewModel() {
    val isDarkMode: LiveData<Boolean> = eventRepository.getIsDarkMode().asLiveData()
    val isNotificationEnabled: LiveData<Boolean> =
        eventRepository.getIsDailyReminderEnabled().asLiveData()

    fun setIsDarkMode(isDarkMode: Boolean) = viewModelScope.launch {
        eventRepository.setIsDarkMode(isDarkMode)
    }

    fun setDailyReminderEnabled(enabled: Boolean) {
        viewModelScope.launch {
            eventRepository.setIsDailyReminderEnabled(enabled)
            if (enabled) {
                scheduleReminderWorker()
            } else {
                cancelReminderWorker()
            }
        }
    }

    private fun scheduleReminderWorker() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val request = PeriodicWorkRequestBuilder<NotificationWorker>(
            DAILY_REMINDER_INTERVAL,
            TimeUnit.DAYS
        )
            .setConstraints(constraints)
            .build()

        workManager.enqueueUniquePeriodicWork(
            DAILY_REMINDER_WORK_NAME,
            ExistingPeriodicWorkPolicy.UPDATE,
            request
        )
    }

    private fun cancelReminderWorker() {
        workManager.cancelUniqueWork(DAILY_REMINDER_WORK_NAME)
    }

    companion object {
        private const val DAILY_REMINDER_INTERVAL = 1L
//        private val DAILY_REMINDER_INTERVAL_UNIT = TimeUnit.DAYS

        private const val DAILY_REMINDER_WORK_NAME = "EventDailyReminderWork"
    }
}