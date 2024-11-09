package com.manikandareas.stories.story.presentation.create_story

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manikandareas.stories.auth.data.preference.PreferenceDataSource
import com.manikandareas.stories.core.domain.util.onError
import com.manikandareas.stories.core.domain.util.onSuccess
import com.manikandareas.stories.core.navigation.Destination
import com.manikandareas.stories.core.navigation.Navigator
import com.manikandareas.stories.story.data.mappers.toStoryDto
import com.manikandareas.stories.story.domain.StoryDataSource
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class CreateStoryViewModel(
    private val storyDataSource: StoryDataSource,
    private val preferenceDataSource: PreferenceDataSource,
    private val navigator: Navigator
) : ViewModel() {
    var state by mutableStateOf(CreateStoryState())
        private set

    private val _events = Channel<CreateStoryEvent>()
    val events = _events.receiveAsFlow()

    private val tokenFlow: Flow<String?> = preferenceDataSource.sessionToken

    fun onAction(action: CreateStoryAction) {
        when (action) {

            CreateStoryAction.MoveToStoryList -> viewModelScope.launch() {
                navigator.navigate(
                    destination = Destination.ListStoryScreen,
                    navOptions = {
                        popUpTo(Destination.ListStoryScreen) {
                            inclusive = true
                        }
                    }
                )
            }

            is CreateStoryAction.OnCreateStoryClicked -> {
                createStory(action.context)
            }

            is CreateStoryAction.OnDescriptionChanged -> {
                state = state.copy(description = action.description)
            }

            is CreateStoryAction.OnCameraClicked -> viewModelScope.launch {
                _events.send(CreateStoryEvent.OpenCamera)
            }

            is CreateStoryAction.OnGalleryClicked -> viewModelScope.launch {
                _events.send(CreateStoryEvent.OpenGallery)
            }

            is CreateStoryAction.OnCameraSelected -> {
                state = state.copy(imageUri = action.imageUri)
            }

            is CreateStoryAction.OnGallerySelected -> {
                state = state.copy(imageUri = action.imageUri)
            }

            CreateStoryAction.OnXClicked -> {
                state = state.copy(imageUri = null)
            }

            CreateStoryAction.OnCreateAnotherStoryClicked -> {
                state = CreateStoryState()
            }
        }
    }

    fun createStory(context: Context) {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val token = tokenFlow.first() ?: return@launch
            val request = state.toStoryDto(context)

            storyDataSource.createStory(token, request).onSuccess {
                state = state.copy(isLoading = false)
                _events.send(CreateStoryEvent.Success)
            }.onError {
                state = state.copy(isLoading = false)
                _events.send(CreateStoryEvent.Error(it))
            }
        }
    }
}