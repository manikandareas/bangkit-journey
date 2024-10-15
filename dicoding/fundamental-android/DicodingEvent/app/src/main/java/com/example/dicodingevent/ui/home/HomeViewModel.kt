package com.example.dicodingevent.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dicodingevent.data.response.ListEvents
import com.example.dicodingevent.data.retrofit.ApiConfig
import com.example.dicodingevent.data.retrofit.EventStatus
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

//    private val _events = MutableLiveData<ListEvents?>()
//    val events: LiveData<ListEvents?> = _events

    private val _upcomingEventsIsLoading = MutableLiveData<Boolean>()
    val upcomingEventsIsLoading: LiveData<Boolean> = _upcomingEventsIsLoading

    private val _finishedEventsIsLoading = MutableLiveData<Boolean>()
    val finishedEventsIsLoading: LiveData<Boolean> = _finishedEventsIsLoading


    private val _upcomingEvents = MutableLiveData<ListEvents?>()
    val upcomingEvents: LiveData<ListEvents?> = _upcomingEvents

    private val _pastEvents = MutableLiveData<ListEvents?>()
    val pastEvents: LiveData<ListEvents?> = _pastEvents

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    init {
        fetchUpcomingEvents()
        fetchPastEvents()
    }


    private fun fetchUpcomingEvents() {
        viewModelScope.launch {
            _upcomingEventsIsLoading.value = true
            _error.value = null

            try {
                val response = ApiConfig.getApiService()
                    .getListEvents(active = EventStatus.UPCOMING, limit = 5, query = "")
                Log.i(TAG, "isSuccessful ${response.isSuccessful}")
                if (response.isSuccessful) {
                    Log.i(TAG, "onResponse: ${response.body()}")
                    val responseEvents = response.body()
                    _upcomingEvents.postValue(responseEvents)
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    _error.postValue("Failed to fetch upcoming events: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "onFailure: ${e.message}")
                _error.postValue("Error: ${e.message}")
            } finally {
                _upcomingEventsIsLoading.value = false
            }
        }
    }

    private fun fetchPastEvents() {
        viewModelScope.launch {
            _finishedEventsIsLoading.value = true
            _error.value = null

            try {
                val response = ApiConfig.getApiService()
                    .getListEvents(active = EventStatus.PAST, limit = 10, query = "")
                Log.i(TAG, "isSuccessful ${response.isSuccessful}")
                if (response.isSuccessful) {
                    Log.i(TAG, "onResponse: ${response.body()}")
                    val responseEvents = response.body()
                    _pastEvents.postValue(responseEvents)
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    _error.postValue("Failed to fetch past events: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "onFailure: ${e.message}")
                _error.postValue("Error: ${e.message}")
            } finally {
                _finishedEventsIsLoading.value = false
            }
        }
    }

    companion object {
        private const val TAG = "HomeViewModel"
    }
}