package com.example.dicodingevent.ui.bookmark

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dicodingevent.data.response.Event
import com.example.dicodingevent.ui.common.EventUtils
import com.example.dicodingevent.ui.common.ListAdapter

class BookmarkViewModel : ViewModel() {
    private val _bookmarkedEvents = MutableLiveData<MutableList<Event>?>()
    val bookmarkedEvents: LiveData<MutableList<Event>?> = _bookmarkedEvents

   private val _toastMessage = MutableLiveData<EventUtils<String?>>()
    val toastMessage: LiveData<EventUtils<String?>> = _toastMessage


    fun addBookmark(event: Event) {
        val currentList = _bookmarkedEvents.value ?: mutableListOf()
        if (!currentList.any { it.id == event.id }) {  // Hindari duplikasi berdasarkan ID
            currentList.add(event)
            _bookmarkedEvents.value = currentList
        }

        Log.i("BookmarkViewModel", "addBookmark: ${_bookmarkedEvents.value}")
    }

    fun removeBookmark(event: Event) {
        val currentList = _bookmarkedEvents.value
        currentList?.let { it ->
            it.removeIf { it.id == event.id } // Hapus event berdasarkan ID
            _bookmarkedEvents.value = currentList
        }
        Log.i("BookmarkViewModel", "addBookmark: ${_bookmarkedEvents.value}")
    }

    private fun checkIsBookmarked(event: Event): Boolean {
        return _bookmarkedEvents.value?.any { it.id == event.id } ?: false
    }

    val bookmarkHandler = object : ListAdapter.BookmarkListener {
        override fun onBookmarkClick(event: Event) {
            Log.i("SearchFragment", "onBookmarkClick: ${event.id} clicked")
            if (isBookmarked(event)) {
                removeBookmark(event)
                _toastMessage.value = EventUtils<String>("Bookmark removed")
            } else {
                addBookmark(event)
                _toastMessage.value = EventUtils<String>("Bookmark added")
            }
        }

        override fun isBookmarked(event: Event): Boolean {
            return checkIsBookmarked(event)
        }
    }

}