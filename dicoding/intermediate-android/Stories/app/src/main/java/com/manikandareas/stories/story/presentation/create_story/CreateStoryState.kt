package com.manikandareas.stories.story.presentation.create_story

import android.net.Uri
import com.manikandareas.stories.story.presentation.models.StoryUi
import java.io.File


data class CreateStoryState(
    val isLoading: Boolean = false,
    val description: String = "",
    val descriptionError: String? = null,
    val imageUri: Uri? = null,
)
