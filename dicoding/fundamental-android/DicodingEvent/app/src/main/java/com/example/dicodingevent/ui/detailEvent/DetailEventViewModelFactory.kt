package com.example.dicodingevent.ui.detailEvent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class DetailEventViewModelFactory (private val eventId : Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailEventViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DetailEventViewModel(eventId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}