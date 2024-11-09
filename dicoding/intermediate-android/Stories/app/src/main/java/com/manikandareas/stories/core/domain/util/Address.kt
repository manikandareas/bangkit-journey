package com.manikandareas.stories.core.domain.util

import kotlinx.serialization.Serializable

@Serializable
data class Address(
    val countryName: String?,
    val countryCode: String?,
    val latitude: Double?,
    val longitude: Double?
)