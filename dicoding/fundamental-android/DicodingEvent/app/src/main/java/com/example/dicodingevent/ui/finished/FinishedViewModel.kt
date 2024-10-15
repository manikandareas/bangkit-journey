package com.example.dicodingevent.ui.finished

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dicodingevent.data.response.ListEvents
import com.example.dicodingevent.data.retrofit.ApiConfig
import com.example.dicodingevent.data.retrofit.EventStatus

import kotlinx.coroutines.launch

class FinishedViewModel : ViewModel() {

    private val _finishedEvents = MutableLiveData<ListEvents?>()
    val finishedEvents:LiveData<ListEvents?> = _finishedEvents

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private fun fetchFinishedEvents() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val response = ApiConfig.getApiService()
                    .getListEvents(active = EventStatus.PAST, limit = 40, query = "")
                Log.i(TAG, "isSuccessful ${response.isSuccessful}")
                if (response.isSuccessful) {
                    Log.i(TAG, "onResponse: ${response.body()}")
                    val responseEvents = response.body()
                    _finishedEvents.postValue(responseEvents)
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    _error.postValue("Failed to fetch list events: ${response.message()}")
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
        fetchFinishedEvents()
    }


    companion object {
        private const val TAG = "FinishedViewModel"
    }
}