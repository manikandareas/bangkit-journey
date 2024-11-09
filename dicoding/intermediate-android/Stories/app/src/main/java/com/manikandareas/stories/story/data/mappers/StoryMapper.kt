package com.manikandareas.stories.story.data.mappers

import android.content.Context
import com.manikandareas.stories.core.presentation.util.ImageUtils
import com.manikandareas.stories.story.data.networking.dto.AddStoryRequestDto
import com.manikandareas.stories.story.data.networking.dto.StoryDto
import com.manikandareas.stories.story.domain.Story
import com.manikandareas.stories.story.presentation.create_story.CreateStoryState

fun StoryDto.toStory(): Story {
    return Story(
        description = description,
        id = id,
        name = name,
        photoUrl = photoUrl,
        createdAt = createdAt,
        lat = lat,
        lon = lon
    )
}

fun CreateStoryState.toStoryDto(context: Context): AddStoryRequestDto {
    val fileImage =
        ImageUtils.uriToFile(imageUri!!, context) ?: throw IllegalStateException("Image not found")
    return AddStoryRequestDto(
        description = description,
        photo = fileImage,

        )
}