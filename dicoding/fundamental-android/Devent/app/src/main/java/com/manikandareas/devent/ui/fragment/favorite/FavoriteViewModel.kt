package com.manikandareas.devent.ui.fragment.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manikandareas.devent.domain.models.EventModel
import com.manikandareas.devent.domain.repositories.EventRepository
import com.manikandareas.devent.utils.AppException
import com.manikandareas.devent.utils.SingleLiveEvent
import com.manikandareas.devent.utils.UIState
import kotlinx.coroutines.launch

class FavoriteViewModel(
    private val eventRepository: EventRepository
) : ViewModel() {
    private val _favoriteEvent = MutableLiveData<UIState<List<EventModel>>>()
    val favoriteEvent: LiveData<UIState<List<EventModel>>> = _favoriteEvent

    private val _toastEvent = SingleLiveEvent<String>()
    val toastEvent: LiveData<String> = _toastEvent

    fun getFavoriteEvent() = viewModelScope.launch {
        _favoriteEvent.postValue(UIState.Loading)
        try {
            val favoriteEventList = eventRepository.getEventFavorite()
            _favoriteEvent.postValue(UIState.Success(favoriteEventList))
        } catch (e: AppException) {
            _toastEvent.postValue(e.message)
            UIState.Error
        } catch (e: Exception) {
            _toastEvent.postValue(e.message)
            UIState.Error
        }
    }

    fun updateEventFavorite(event: EventModel) = viewModelScope.launch {
        try {
            eventRepository.updateEventFavorite(event)
            getFavoriteEvent()
            val message = if (event.isFavorite) "Event has been added to favorite" else "Event has been removed from favorite"
            _toastEvent.postValue(message)
        } catch (e: AppException) {
            _toastEvent.postValue(e.message)
        } catch (e: Exception) {
            _toastEvent.postValue(e.message)
        }
    }
}