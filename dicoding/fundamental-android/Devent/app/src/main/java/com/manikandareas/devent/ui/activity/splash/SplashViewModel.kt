package com.manikandareas.devent.ui.activity.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.manikandareas.devent.domain.repositories.EventRepository

class SplashViewModel(eventRepository: EventRepository) : ViewModel() {
    val isDarkMode = eventRepository.getIsDarkMode().asLiveData()
}