package com.manikandareas.stories.story.data.networking.dto

import kotlinx.serialization.Serializable


@Serializable
data class StoryDto(
    val id: String,
    val name: String,
    val description: String,
    val photoUrl: String,
    val createdAt: String,
    val lat: Double? = null,
    val lon: Double? = null
)

