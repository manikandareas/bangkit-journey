package com.manikandareas.stories.story.presentation.story_list

import com.manikandareas.stories.story.presentation.models.StoryUi

data class StoryListState(
    val isLoading: Boolean = false,
    val stories: List<StoryUi> = emptyList(),
    val selectedStory: StoryUi? = null,
)
