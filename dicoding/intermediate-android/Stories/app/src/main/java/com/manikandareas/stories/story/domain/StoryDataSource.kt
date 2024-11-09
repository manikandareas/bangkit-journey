package com.manikandareas.stories.story.domain

import com.manikandareas.stories.core.domain.util.NetworkError
import com.manikandareas.stories.core.domain.util.Result
import com.manikandareas.stories.story.data.networking.dto.AddStoryRequestDto

interface StoryDataSource {
    suspend fun getStories( token: String): Result<List<Story>, NetworkError>

    suspend fun createStory(token: String, request: AddStoryRequestDto): Result<Unit, NetworkError>

    suspend fun getDetailStory (token: String, storyId: String): Result<Story, NetworkError>
}
