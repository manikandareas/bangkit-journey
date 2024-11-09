package com.manikandareas.stories.story.presentation.story_detail

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manikandareas.stories.auth.data.preference.PreferenceDataSource
import com.manikandareas.stories.core.domain.util.onError
import com.manikandareas.stories.core.domain.util.onSuccess
import com.manikandareas.stories.core.navigation.Navigator
import com.manikandareas.stories.story.domain.StoryDataSource
import com.manikandareas.stories.story.presentation.models.toStoryUi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StoryDetailViewModel(
    private val storyDataSource: StoryDataSource,
    private val preferenceDataSource: PreferenceDataSource,
    private val storyId: String,
    private val navigator: Navigator
) : ViewModel() {
    private val _state = MutableStateFlow(StoryDetailState())
    val state = _state.onStart {
        loadStory()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), StoryDetailState())

    private val _events = Channel<StoryDetailEvent>()
    val events = _events.receiveAsFlow()

    private val tokenFlow: Flow<String?> = preferenceDataSource.sessionToken

    fun onAction(action: StoryDetailAction) {
        when (action) {
            is StoryDetailAction.LoadStory -> {

            }

            StoryDetailAction.OnBackClicked -> viewModelScope.launch {
                navigator.navigateUp()
            }
        }
    }

    private fun loadStory() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true,
                )
            }

            val token = tokenFlow.first() ?: ""
            storyDataSource.getDetailStory(token = token, storyId = storyId).onSuccess { res ->
                _state.update { st ->
                    st.copy(
                        isLoading = false,
                        story = res.toStoryUi()
                    )
                }
            }.onError { error ->
                _state.update { it.copy(isLoading = false) }
                _events.send(StoryDetailEvent.Error(error = error))
            }
        }
    }
}