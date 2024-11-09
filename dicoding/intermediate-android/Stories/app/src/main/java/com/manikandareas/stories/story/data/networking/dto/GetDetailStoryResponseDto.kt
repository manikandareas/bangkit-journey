package com.manikandareas.stories.story.data.networking.dto

import kotlinx.serialization.Serializable

@Serializable
data class GetDetailStoryResponseDto (
    val error : Boolean,
    val message: String,
    val story: StoryDto
)