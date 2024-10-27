package com.manikandareas.devent.ui.activity.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.manikandareas.devent.domain.repositories.EventRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainViewModel(private val eventRepository: EventRepository) : ViewModel() {
    val isFirstTime = eventRepository.getIsFirstTime().asLiveData()
    val isDarkMode = eventRepository.getIsDarkMode().asLiveData()

    fun setIsDarkMode(isDarkMode: Boolean) = viewModelScope.launch {
        eventRepository.setIsDarkMode(isDarkMode)
    }

    fun setIsFirstTime(isFirstTime: Boolean) = viewModelScope.launch {
        eventRepository.setIsFirstTime(isFirstTime)
    }
}