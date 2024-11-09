package com.manikandareas.stories.auth.domain

import com.manikandareas.stories.auth.data.networking.dto.LoginRequestDto
import com.manikandareas.stories.auth.data.networking.dto.LoginResponseDto
import com.manikandareas.stories.auth.data.networking.dto.RegisterRequestDto
import com.manikandareas.stories.auth.data.networking.dto.RegisterResponseDto
import com.manikandareas.stories.core.domain.util.NetworkError
import com.manikandareas.stories.core.domain.util.Result

interface AuthDataSource{
    suspend fun register(request: RegisterRequestDto): Result<RegisterResponseDto, NetworkError>
    suspend fun login(request: LoginRequestDto): Result<LoginResponseDto, NetworkError>
    suspend fun logout(): Boolean
}