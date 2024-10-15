package com.example.dicodingevent.ui.detailEvent

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dicodingevent.data.response.DetailEvent
import com.example.dicodingevent.data.retrofit.ApiConfig
import kotlinx.coroutines.launch

class DetailEventViewModel(private val eventID: Int) : ViewModel() {

    private val _event = MutableLiveData<DetailEvent?>()
    val event: LiveData<DetailEvent?> = _event

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    init {
        fetchDetailEvent()
    }

    private fun fetchDetailEvent() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val response = ApiConfig.getApiService()
                    .getDetailEvent(eventID)
                Log.i(TAG, "isSuccessful ${response.isSuccessful}")
                if (response.isSuccessful) {
                    Log.i(TAG, "onResponse: ${response.body()}")
                    val responseEvents = response.body()
                    _event.postValue(responseEvents)
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    _error.postValue("Failed to fetch detail event: ${response.message()}")
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
        private const val TAG = "DetailEventViewModel"
    }
}