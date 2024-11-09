package com.manikandareas.stories.story.data.networking.dto

import kotlinx.serialization.Serializable

@Serializable
data class AllStoriesParameters(
    val page: Int? = null,
    val size: Int? = null,
    val location: Int? = null
)

object StoryLocation {
    const val WITH_LOCATION = 1
    const val WITHOUT_LOCATION = 0
}