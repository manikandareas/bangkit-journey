package com.example.dicodingevent.ui.upcoming

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dicodingevent.data.response.ListEvents
import com.example.dicodingevent.data.retrofit.ApiConfig
import com.example.dicodingevent.data.retrofit.EventStatus
import kotlinx.coroutines.launch

class UpcomingViewModel : ViewModel() {


    private val _upcomingEvents = MutableLiveData<ListEvents?>()
    val upcomingEvents:LiveData<ListEvents?> = _upcomingEvents

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private fun fetchUpcomingEvents() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val response = ApiConfig.getApiService()
                    .getListEvents(active = EventStatus.UPCOMING, limit = 40, query = "")
                Log.i(TAG, "isSuccessful ${response.isSuccessful}")
                if (response.isSuccessful) {
                    Log.i(TAG, "onResponse: ${response.body()}")
                    val responseEvents = response.body()
                    _upcomingEvents.postValue(responseEvents)
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    _error.postValue("Failed to fetch list upcoming events: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "onFailure: ${e.message}")
                _error.postValue("Error: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    init {
        fetchUpcomingEvents()
    }


    companion object {
        private const val TAG = "UpcomingViewModel"
    }
}