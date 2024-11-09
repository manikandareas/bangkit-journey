package com.manikandareas.stories.story.presentation.story_list

import com.manikandareas.stories.core.domain.util.NetworkError

sealed interface StoryListEvent {
    data class Error(val error: NetworkError) : StoryListEvent
    object Logout : StoryListEvent
}