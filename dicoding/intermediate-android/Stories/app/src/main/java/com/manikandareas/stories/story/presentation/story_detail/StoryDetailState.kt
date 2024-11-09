package com.manikandareas.stories.story.presentation.story_detail

import com.manikandareas.stories.story.presentation.models.RawAddress
import com.manikandareas.stories.story.presentation.models.StoryUi
import java.time.ZonedDateTime


data class StoryDetailState(
    val story: StoryUi = StoryUi(
        id = "",
        description = "",
        address = RawAddress(
            latitude = null,
            longitude = null
        ),
        photoUrl = "",
        createdAt = ZonedDateTime.now(),
        name = ""
    ),
    val isLoading: Boolean = false,
)
