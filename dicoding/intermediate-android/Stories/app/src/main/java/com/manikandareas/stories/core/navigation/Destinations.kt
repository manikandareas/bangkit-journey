package com.manikandareas.stories.core.navigation

import kotlinx.serialization.Serializable

sealed interface Destination {
    @Serializable
    data object AuthGraph : Destination

    @Serializable
    data object LoginScreen: Destination

    @Serializable
    data object RegisterScreen: Destination

    @Serializable
    data object HomeGraph: Destination

    @Serializable
    data object ListStoryScreen: Destination

    @Serializable
    data object AddStoryScreen: Destination

    @Serializable
    data class StoryDetailScreen(val storyId: String): Destination
}