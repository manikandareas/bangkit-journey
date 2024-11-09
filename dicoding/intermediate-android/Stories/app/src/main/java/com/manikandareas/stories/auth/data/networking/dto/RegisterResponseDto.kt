package com.manikandareas.stories.auth.data.networking.dto

import kotlinx.serialization.Serializable

@Serializable
data class RegisterResponseDto(
    val error: Boolean,
    val message: String
)
