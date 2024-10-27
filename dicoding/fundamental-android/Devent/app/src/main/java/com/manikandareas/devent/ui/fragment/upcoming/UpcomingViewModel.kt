package com.manikandareas.devent.ui.fragment.upcoming

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manikandareas.devent.domain.models.EventModel
import com.manikandareas.devent.domain.repositories.EventRepository
import com.manikandareas.devent.utils.AppException
import com.manikandareas.devent.utils.SingleLiveEvent
import com.manikandareas.devent.utils.UIState
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class UpcomingViewModel(
    private val eventRepository: EventRepository
) : ViewModel() {
    private val _upcomingEvent = MutableLiveData<UIState<List<EventModel>>>()
    val upcomingEvent: LiveData<UIState<List<EventModel>>> = _upcomingEvent

    private val _toastEvent = SingleLiveEvent<String>()
    val toastEvent: LiveData<String> = _toastEvent

    private var fetchJob: Job? = null

    fun fetchEvent(query: String = "") {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            _upcomingEvent.postValue(UIState.Loading)

            val upcomingEventResult = async {
                try {
                    val upcomingEventList = eventRepository.fetchEvent(1, query)
                    UIState.Success(upcomingEventList)
                } catch (e: AppException) {
                    _toastEvent.postValue(e.message)
                    UIState.Error
                } catch (e: Exception) {
                    _toastEvent.postValue(e.message)
                    UIState.Error
                }
            }

            _upcomingEvent.postValue(upcomingEventResult.await())
        }
    }

    fun getUpcomingEvent() = viewModelScope.launch {
        _upcomingEvent.postValue(UIState.Loading)
        try {
            val upcomingEventList = async { eventRepository.getUpcomingEvent() }
            upcomingEventList.await().let { eventList ->
                if (eventList.isEmpty()) {
                    fetchEvent()
                } else {
                    _upcomingEvent.postValue(UIState.Success(eventList))
                }
            }
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
            getUpcomingEvent()
            val message = if (event.isFavorite) "Event has been added to favorite" else "Event has been removed from favorite"
            _toastEvent.postValue(message)
        } catch (e: AppException) {
            _toastEvent.postValue(e.message)
        } catch (e: Exception) {
            _toastEvent.postValue(e.message)
        }
    }
}