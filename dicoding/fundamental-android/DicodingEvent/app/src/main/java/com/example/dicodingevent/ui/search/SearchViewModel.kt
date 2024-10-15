package com.example.dicodingevent.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dicodingevent.data.response.ListEvents
import com.example.dicodingevent.data.retrofit.ApiConfig
import com.example.dicodingevent.data.retrofit.EventStatus
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {
    private val _searchEvents = MutableLiveData<ListEvents?>()
    val searchEvents: LiveData<ListEvents?> = _searchEvents

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun findEvents(q: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val response = ApiConfig.getApiService()
                    .getListEvents(active = EventStatus.ALL, limit = 40, query = q)
                Log.i(TAG, "isSuccessful ${response.isSuccessful}")
                if (response.isSuccessful) {
                    Log.i(TAG, "onResponse: ${response.body()}")
                    val responseEvents = response.body()
                    _searchEvents.postValue(responseEvents)
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    _error.postValue("Failed to find events: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "onFailure: ${e.message}")
                _error.postValue("Error: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }




    companion object {
        private const val TAG = "SearchViewModel"
    }
}