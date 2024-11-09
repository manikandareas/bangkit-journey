package com.manikandareas.stories.story.presentation.create_story

import android.content.Context
import android.net.Uri

sealed class CreateStoryAction {
    object OnGalleryClicked : CreateStoryAction()
    object OnCameraClicked : CreateStoryAction()
    object OnXClicked : CreateStoryAction()

    data class OnGallerySelected(val imageUri: Uri) : CreateStoryAction()

    data class OnCameraSelected(val imageUri: Uri) : CreateStoryAction()

    data class OnDescriptionChanged(val description: String) : CreateStoryAction()

    data class OnCreateStoryClicked(val context: Context) : CreateStoryAction()

    object MoveToStoryList : CreateStoryAction()

    object OnCreateAnotherStoryClicked : CreateStoryAction()
}