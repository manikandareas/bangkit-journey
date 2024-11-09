package com.manikandareas.stories.story.data.networking.dto

import kotlinx.serialization.Serializable

@Serializable
data class AllStoriesResponseDto (
    val error : Boolean,
    val message: String,
    val listStory: List<StoryDto>
)