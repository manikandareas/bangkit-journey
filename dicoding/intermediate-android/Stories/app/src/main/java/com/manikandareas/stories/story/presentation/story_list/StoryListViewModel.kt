package com.manikandareas.stories.story.presentation.story_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manikandareas.stories.auth.data.preference.PreferenceDataSource
import com.manikandareas.stories.core.domain.util.onError
import com.manikandareas.stories.core.domain.util.onSuccess
import com.manikandareas.stories.core.navigation.Destination
import com.manikandareas.stories.core.navigation.Destination.*
import com.manikandareas.stories.core.navigation.Navigator
import com.manikandareas.stories.story.domain.StoryDataSource
import com.manikandareas.stories.story.presentation.models.toStoryUi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StoryListViewModel(
    private val storyDataSource: StoryDataSource,
    private val preferenceDataSource: PreferenceDataSource,
    private val navigator: Navigator,
) : ViewModel() {

    private val _state = MutableStateFlow(StoryListState())
    val state = _state
        .onStart { loadStories() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), StoryListState())

    private val _events = Channel<StoryListEvent>()
    val events = _events.receiveAsFlow()

    private val tokenFlow: Flow<String?> = preferenceDataSource.sessionToken


    fun onAction(action: StoryListAction) {
        when (action) {
            is StoryListAction.OnStoryListClick -> viewModelScope.launch {
                navigator.navigate(StoryDetailScreen(storyId = action.storyId))
            }

            is StoryListAction.LoadStories -> {}

            StoryListAction.MoveToCreateStory -> viewModelScope.launch {
                navigator.navigate(Destination.AddStoryScreen)
            }

            StoryListAction.OnLogoutClicked -> {
                viewModelScope.launch {
                    _events.send(StoryListEvent.Logout)
                }
            }

            StoryListAction.OnConfirmLogoutClicked -> {
                viewModelScope.launch {
                    preferenceDataSource.setSessionToken("")
                    navigator.navigate(Destination.LoginScreen)
                }
            }
        }
    }

    private fun loadStories() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true,
                )
            }

            val token = tokenFlow.first() ?: ""
            storyDataSource.getStories(token = token).onSuccess { res ->
                _state.update { st ->
                    st.copy(
                        isLoading = false,
                        stories = res.map { it.toStoryUi() },
                    )
                }
            }.onError { error ->
                _state.update { it.copy(isLoading = false) }
                _events.send(StoryListEvent.Error(error = error))
            }
        }
    }
}