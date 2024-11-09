package com.manikandareas.stories.auth.data.networking

import com.manikandareas.stories.auth.data.networking.dto.LoginRequestDto
import com.manikandareas.stories.auth.data.networking.dto.LoginResponseDto
import com.manikandareas.stories.auth.data.networking.dto.RegisterRequestDto
import com.manikandareas.stories.auth.data.networking.dto.RegisterResponseDto
import com.manikandareas.stories.auth.domain.AuthDataSource
import com.manikandareas.stories.core.data.networking.constructUrl
import com.manikandareas.stories.core.data.networking.safeCall
import com.manikandareas.stories.core.domain.util.NetworkError
import com.manikandareas.stories.core.domain.util.Result
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class RemoteAuthDataSource(private val httpClient: HttpClient) : AuthDataSource {
    override suspend fun register(request: RegisterRequestDto): Result<RegisterResponseDto, NetworkError> {
        return safeCall<RegisterResponseDto> {
            httpClient.post(
                urlString = constructUrl("/register")
            ) {
                setBody(
                    RegisterRequestDto(
                        name = request.name,
                        email = request.email,
                        password = request.password
                    )
                )
            }
        }
    }

    override suspend fun login(
        request: LoginRequestDto
    ): Result<LoginResponseDto, NetworkError> {
        return safeCall<LoginResponseDto> {
            httpClient.post(
                urlString = constructUrl("/login")
            ) {
                setBody(
                    LoginRequestDto(
                        email = request.email,
                        password = request.password
                    )
                )
            }
        }
    }

    override suspend fun logout(): Boolean {
        TODO("Not yet implemented")
    }
}