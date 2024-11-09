package com.manikandareas.stories.story.presentation.story_detail

import android.content.Context

sealed class StoryDetailAction {
    data class LoadStory(val context: Context) : StoryDetailAction()

    object OnBackClicked : StoryDetailAction()
}