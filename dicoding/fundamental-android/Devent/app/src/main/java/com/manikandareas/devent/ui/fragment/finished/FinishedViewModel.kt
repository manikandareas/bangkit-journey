package com.manikandareas.devent.ui.fragment.finished

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manikandareas.devent.domain.models.EventModel
import com.manikandareas.devent.domain.models.StatusEvent
import com.manikandareas.devent.domain.repositories.EventRepository
import com.manikandareas.devent.utils.AppException
import com.manikandareas.devent.utils.SingleLiveEvent
import com.manikandareas.devent.utils.UIState
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FinishedViewModel(
    private val eventRepository: EventRepository
) : ViewModel() {
    private val _finishedEvent = MutableLiveData<UIState<List<EventModel>>>()
    val finishedEvent: LiveData<UIState<List<EventModel>>> = _finishedEvent

    private val _toastEvent = SingleLiveEvent<String>()
    val toastEvent: LiveData<String> = _toastEvent

    private var fetchJob: Job? = null

   private fun fetchEvent(query : String = "") {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            _finishedEvent.postValue(UIState.Loading)

            val finishedEventResult = async {
                try {
                    val finishedEventList = eventRepository.fetchEvent(StatusEvent.FINISHED, query)
                    UIState.Success(finishedEventList)
                } catch (e: AppException) {
                    _toastEvent.postValue(e.message)
                    UIState.Error
                } catch (e: Exception) {
                    _toastEvent.postValue(e.message)
                    UIState.Error
                }
            }

            _finishedEvent.postValue(finishedEventResult.await())
        }
    }

    fun getFinishedEvent() = viewModelScope.launch {
        _finishedEvent.postValue(UIState.Loading)
        try {
            val finishedEventList = async { eventRepository.getFinishedEvent() }
            finishedEventList.await().let { eventList ->
                if (eventList.isEmpty()) {
                    fetchEvent()
                } else {
                    _finishedEvent.postValue(UIState.Success(eventList))
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
            val message = if (event.isFavorite) "Event has been added to favorite" else "Event has been removed from favorite"
            _toastEvent.postValue(message)
        } catch (e: AppException) {
            _toastEvent.postValue(e.message)
        } catch (e: Exception) {
            _toastEvent.postValue("An unexpected error occurred")
        }
    }
}