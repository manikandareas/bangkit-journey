package com.manikandareas.devent.ui.fragment.explore

import android.util.Log
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.cancellation.CancellationException

class ExploreViewModel(private val eventRepository: EventRepository) : ViewModel() {

    private val _upcomingEvent = MutableLiveData<UIState<List<EventModel>>>()
    val upcomingEvent: LiveData<UIState<List<EventModel>>> = _upcomingEvent

    private val _finishedEvent = MutableLiveData<UIState<List<EventModel>>>()
    val finishedEvent: LiveData<UIState<List<EventModel>>> = _finishedEvent

    private val _searchEvent = MutableLiveData<UIState<List<EventModel>>>()
    val searchEvent: LiveData<UIState<List<EventModel>>> = _searchEvent

    private val _sizeFavorite = MutableLiveData<Int>()
    val sizeFavorite: LiveData<Int> = _sizeFavorite

    private val _toastEvent = SingleLiveEvent<String>()
    val toastEvent: LiveData<String> = _toastEvent

    private var fetchJob: Job? = null

    private var searchJob: Job? = null

    private fun fetchEvent() {
        if (fetchJob?.isActive == true) return

        fetchJob = viewModelScope.launch {
            _upcomingEvent.postValue(UIState.Loading)
            _finishedEvent.postValue(UIState.Loading)

            val upcomingEventResult = async {
                try {
                    val upcomingEventList = eventRepository.fetchEvent(StatusEvent.ACTIVE).take(5)
                    UIState.Success(upcomingEventList)
                } catch (e: AppException) {
                    _toastEvent.postValue(e.message)
                    UIState.Error
                } catch (e: Exception) {
                    _toastEvent.postValue(e.message)
                    UIState.Error
                }
            }

            val finishedEventResult = async {
                try {
                    val finishedEventList = eventRepository.fetchEvent(StatusEvent.FINISHED).take(7)
                    UIState.Success(finishedEventList)
                } catch (e: AppException) {
                    _toastEvent.postValue(e.message)
                    UIState.Error
                } catch (e: Exception) {
                    _toastEvent.postValue(e.message)
                    UIState.Error
                }
            }

            _upcomingEvent.postValue(upcomingEventResult.await())
            _finishedEvent.postValue(finishedEventResult.await())
        }
    }

    fun getUpcomingEvent() = viewModelScope.launch {
        _upcomingEvent.postValue(UIState.Loading)
        try {
            val upcomingEventList = async { eventRepository.getUpcomingEvent().take(5) }
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

    fun getFinishedEvent() = viewModelScope.launch {
        _finishedEvent.postValue(UIState.Loading)
        try {
            val upcomingEventList = async { eventRepository.getFinishedEvent().take(5) }
            upcomingEventList.await().let { eventList ->
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

    fun getSizeFavorite() = viewModelScope.launch {
        try {
            val sizeFavorite = eventRepository.getEventFavorite().size
            _sizeFavorite.postValue(sizeFavorite)
        } catch (e: AppException) {
            _toastEvent.postValue(e.message)
        } catch (e: Exception) {
            _toastEvent.postValue(e.message)
        }
    }

    fun updateEventFavorite(event: EventModel) = viewModelScope.launch {
        try {
            eventRepository.updateEventFavorite(event)
            getSizeFavorite()
            val message =
                if (event.isFavorite) "Event has been added to favorite" else "Event has been removed from favorite"
            _toastEvent.postValue(message)
        } catch (e: AppException) {
            _toastEvent.postValue(e.message)
        } catch (e: Exception) {
            _toastEvent.postValue("An unexpected error occurred")
        }
    }

    fun searchEventWithQuery(q: String) {
        searchJob?.cancel()

        searchJob = viewModelScope.launch {
            try {
                Log.d(TAG, "Starting search for query: $q")
                _searchEvent.value = UIState.Loading

                delay(SEARCH_DELAY)

                if (q.isEmpty()) {
                    Log.d(TAG, "Empty query, clearing search")
                    clearSearchEvent()
                    return@launch
                }

                coroutineScope {
                    Log.d(TAG, "Fetching results from repository")
                    val searchEventList = withContext(Dispatchers.IO) {
                        eventRepository.fetchEvent(StatusEvent.ALL, q)
                    }

                    if (isActive) {
                        Log.d(TAG, "Search completed, found ${searchEventList.size} results")
                        _searchEvent.value = UIState.Success(searchEventList)
                    }
                }
            } catch (e: CancellationException) {
                Log.d(TAG, "Search cancelled")
            } catch (e: Exception) {
                Log.e(TAG, "Search error", e)
                _searchEvent.value = UIState.Error
                _toastEvent.value = e.message ?: "Search failed"
            }
        }
    }

    fun clearSearchEvent() {
        searchJob?.cancel()
        _searchEvent.value = UIState.Success(emptyList())
    }

    companion object {
        private const val TAG = "ExploreViewModel"

        private const val SEARCH_DELAY = 300L
    }
}