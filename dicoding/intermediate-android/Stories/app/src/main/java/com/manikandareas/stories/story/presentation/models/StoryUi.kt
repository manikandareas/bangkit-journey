package com.manikandareas.stories.story.presentation.models

import android.content.Context
import com.manikandareas.stories.core.data.util.getLocationFromCoordinate
import com.manikandareas.stories.core.domain.util.Address
import com.manikandareas.stories.story.domain.Story
import java.time.ZonedDateTime


data class StoryUi(
    val id: String,
    val name: String,
    val description: String,
    val photoUrl: String,
    val address: RawAddress,
    val createdAt: ZonedDateTime = ZonedDateTime.now()
)

data class DisplayableAddress(
    val value: RawAddress,
    val formatted: Address
)

data class RawAddress(
    val latitude: Double?,
    val longitude: Double?
)

suspend fun RawAddress.toDisplayableAddress(context: Context): DisplayableAddress {
    val locationAddress =
        if (latitude != null && longitude != null) getLocationFromCoordinate(context, latitude, longitude) else null
    val address = if (locationAddress != null) locationAddress else Address(
        countryName = null,
        countryCode = null,
        latitude = latitude,
        longitude = longitude
    )
    return DisplayableAddress(this, address)
}


fun Story.toStoryUi(): StoryUi {
    return StoryUi(
        id = id,
        name = name,
        description = description,
        photoUrl = photoUrl,
        createdAt = ZonedDateTime.parse(createdAt),
        address = RawAddress(
            latitude = lat,
            longitude = lon
        ),
    )
}

