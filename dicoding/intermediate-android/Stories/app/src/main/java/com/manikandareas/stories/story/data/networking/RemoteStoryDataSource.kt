package com.manikandareas.stories.story.data.networking

import com.manikandareas.stories.core.data.networking.constructUrl
import com.manikandareas.stories.core.data.networking.safeCall
import com.manikandareas.stories.core.domain.util.NetworkError
import com.manikandareas.stories.core.domain.util.Result
import com.manikandareas.stories.core.domain.util.map
import com.manikandareas.stories.story.data.mappers.toStory
import com.manikandareas.stories.story.data.networking.dto.AddStoryRequestDto
import com.manikandareas.stories.story.data.networking.dto.AddStoryResponseDto
import com.manikandareas.stories.story.data.networking.dto.AllStoriesResponseDto
import com.manikandareas.stories.story.data.networking.dto.GetDetailStoryResponseDto
import com.manikandareas.stories.story.domain.Story
import com.manikandareas.stories.story.domain.StoryDataSource
import io.ktor.client.HttpClient
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.Headers
import io.ktor.util.InternalAPI

@OptIn(InternalAPI::class)
class RemoteStoryDataSource(
    private val httpClient: HttpClient
) : StoryDataSource {

    override suspend fun getStories(token: String): Result<List<Story>, NetworkError> {
        return safeCall<AllStoriesResponseDto> {
            httpClient.get(
                urlString = constructUrl("/stories"),
            ) {
                bearerAuth(token)
            }
        }.map { res -> res.listStory.map { it.toStory() } }
    }

    override suspend fun createStory(
        token: String,
        request: AddStoryRequestDto
    ): Result<Unit, NetworkError> {
        return safeCall<AddStoryResponseDto> {
            httpClient.post(
                urlString = constructUrl("/stories"),
            ) {
                bearerAuth(token)
                setBody(
                    MultiPartFormDataContent(
                        formData {
                            append("description", request.description)
                            append("photo", request.photo.readBytes(), Headers.build {
                                append(
                                    "Content-Disposition",
                                    "form-data; name=photo; filename=${request.photo.name}"
                                )
                            })
                            request.lat?.let { append("lat", it.toString()) }
                            request.lon?.let { append("lon", it.toString()) }
                        },
                    )
                )
            }

        }.map { }
    }

    override suspend fun getDetailStory(
        token: String,
        storyId: String
    ): Result<Story, NetworkError> {
        return safeCall<GetDetailStoryResponseDto> {
            httpClient.get(
                urlString = constructUrl("/stories/$storyId"),
            ) {
                bearerAuth(token)
            }
        }.map {
            it.story.toStory()
        }
    }
}

//override suspend fun register(request: RegisterRequestDto): Result<RegisterResponseDto, NetworkError> {
//    return safeCall<RegisterResponseDto> {
//        httpClient.post(
//            urlString = constructUrl("/register")
//        ) {
//            setBody(
//                RegisterRequestDto(
//                    name = request.name,
//                    email = request.email,
//                    password = request.password
//                )
//            )
//        }
//    }
//}
