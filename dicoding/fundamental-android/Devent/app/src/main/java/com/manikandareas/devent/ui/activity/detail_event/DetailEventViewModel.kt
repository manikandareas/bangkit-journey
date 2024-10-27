package com.manikandareas.devent.ui.activity.detail_event

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manikandareas.devent.domain.models.EventModel
import com.manikandareas.devent.domain.repositories.EventRepository
import com.manikandareas.devent.utils.SingleLiveEvent
import kotlinx.coroutines.launch

class DetailEventViewModel(private val eventRepository: EventRepository) : ViewModel() {
    private val _toastEvent = SingleLiveEvent<String>()
    val toastEvent: LiveData<String> = _toastEvent

    private val _event = MutableLiveData<EventModel>()
    val event: LiveData<EventModel> = _event

    fun updateFavoriteEvent(updatedEvent: EventModel) = viewModelScope.launch {
        eventRepository.updateEventFavorite(updatedEvent)
        updatedEvent.isFavorite = !updatedEvent.isFavorite
        _event.postValue(updatedEvent)
        val message = if (updatedEvent.isFavorite) "Event has been added to favorite" else "Event has been removed from favorite"
        _toastEvent.postValue(message)
    }

    fun setEvent(event: EventModel) {
        _event.postValue(event)
    }
}