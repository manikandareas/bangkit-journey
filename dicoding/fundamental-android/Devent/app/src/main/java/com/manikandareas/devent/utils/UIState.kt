package com.manikandareas.devent.utils

sealed class UIState<out T> {
    data class Success<out T>(val data: T) : UIState<T>()
    data object Error : UIState<Nothing>()
    data object Loading : UIState<Nothing>()
}
