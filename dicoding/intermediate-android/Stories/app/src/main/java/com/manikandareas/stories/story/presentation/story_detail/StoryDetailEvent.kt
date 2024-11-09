package com.manikandareas.stories.story.presentation.story_detail

import com.manikandareas.stories.core.domain.util.NetworkError

sealed interface StoryDetailEvent {
    data class Error(val error:NetworkError): StoryDetailEvent
}