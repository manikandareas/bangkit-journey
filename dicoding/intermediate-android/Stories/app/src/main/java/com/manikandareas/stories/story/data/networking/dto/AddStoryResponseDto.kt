package com.manikandareas.stories.story.data.networking.dto

import kotlinx.serialization.Serializable

@Serializable
data class AddStoryResponseDto  (
    val error : Boolean,
    val message: String
)