@file:OptIn(ExperimentalSharedTransitionApi::class, ExperimentalLayoutApi::class)

package com.manikandareas.stories.story.presentation.components

import android.util.Log
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.manikandareas.stories.R
import com.manikandareas.stories.core.presentation.util.toHumanizedTime
import com.manikandareas.stories.story.presentation.models.DisplayableAddress
import com.manikandareas.stories.story.presentation.models.StoryUi
import com.manikandareas.stories.story.presentation.models.toDisplayableAddress

@Composable
fun StoryItem(
    story: StoryUi,
    modifier: Modifier = Modifier,
    storyImageStyle: Modifier = Modifier,
    storyRowStyle: Modifier = Modifier,
) {
    val context = LocalContext.current

    var address by remember {
        mutableStateOf<DisplayableAddress?>(null)
    }

    LaunchedEffect(story.address) {
        try {
            address = story.address.toDisplayableAddress(context)
        } catch (e: Exception) {
            // Handle error jika perlu
            Log.e("AddressDisplay", "Error loading address", e)
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(272.dp)
            .background(color = MaterialTheme.colorScheme.surface)
            .clip(shape = MaterialTheme.shapes.medium)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(story.photoUrl)
                .crossfade(true)
                .build(),
            contentDescription = "Story Image",
            modifier = storyImageStyle
                .aspectRatio(16 / 9f)
                .height(224.dp)
                .fillMaxWidth()
                .clip(shape = MaterialTheme.shapes.medium),
            contentScale = ContentScale.Crop,
        )

        Row(
            modifier = storyRowStyle
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.icon_me),
                contentDescription = "User Image",
                modifier = Modifier
                    .size(36.dp)
                    .clip(shape = MaterialTheme.shapes.extraLarge),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .weight(1f),
                verticalArrangement = Arrangement.Center,

                ) {
                Text(
                    text = story.name,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyMedium
                )

                if (address?.formatted?.countryName != null) {
                    Text(
                        text = address?.formatted?.countryName ?: "",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            Column(modifier = Modifier, horizontalAlignment = Alignment.End) {
                Text(
                    text = story.createdAt.toHumanizedTime(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

//@Preview
//@Composable
//fun PreviewStoryItem() {
//    StoriesTheme {
//        StoryItem(
//            story = StoryUi(
//                id = "1",
//                name = "Manikandan",
//                photoUrl = "https://images.unsplash.com/photo-1634170380000-7b3b3b3b3b3b",
//                createdAt = ZonedDateTime.now(),
//                address = Address(
//                    countryName = "India",
//                    countryCode = "IN",
//                    latitude = 13.0827,
//                    longitude = 80.2707
//                ),
//                description = ""
//            ),
//        )
//    }
//}
