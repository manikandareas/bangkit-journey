package com.manikandareas.stories.story.presentation.create_story

import com.manikandareas.stories.core.domain.util.NetworkError

sealed interface CreateStoryEvent {
    data class Error(val error: NetworkError) : CreateStoryEvent
    object Success : CreateStoryEvent
    object OpenCamera : CreateStoryEvent
    object OpenGallery : CreateStoryEvent
}