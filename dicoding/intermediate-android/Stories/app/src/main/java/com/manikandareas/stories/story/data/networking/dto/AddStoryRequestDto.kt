package com.manikandareas.stories.story.data.networking.dto

import java.io.File

data class AddStoryRequestDto(
    val description: String,
    val photo: File,
    val lat: Float? = null,
    val lon: Float? = null
)
