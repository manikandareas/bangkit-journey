package com.manikandareas.stories.story.presentation.story_list

import android.content.Context

sealed class StoryListAction {
    data class OnStoryListClick(val storyId: String) : StoryListAction()
    data class LoadStories(val context: Context) : StoryListAction()

    object MoveToCreateStory : StoryListAction()
    object OnLogoutClicked : StoryListAction()
    object OnConfirmLogoutClicked : StoryListAction()
}