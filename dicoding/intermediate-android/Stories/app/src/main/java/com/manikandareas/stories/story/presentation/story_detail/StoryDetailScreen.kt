@file:OptIn(
    ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class,
    ExperimentalLayoutApi::class
)

package com.manikandareas.stories.story.presentation.story_detail

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
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
import com.manikandareas.stories.core.presentation.util.ObserveAsEvents
import com.manikandareas.stories.core.presentation.util.toHumanizedTime
import com.manikandareas.stories.core.presentation.util.toString
import com.manikandareas.stories.story.presentation.models.DisplayableAddress
import com.manikandareas.stories.story.presentation.models.toDisplayableAddress
import kotlinx.coroutines.flow.Flow

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun SharedTransitionScope.StoryDetailScreen(
    animatedVisibilityScope: AnimatedVisibilityScope,
    state: StoryDetailState,
    modifier: Modifier = Modifier,
    events: Flow<StoryDetailEvent>,
    onAction: (StoryDetailAction) -> Unit
) {
    val story = state.story
    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

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

    ObserveAsEvents(events = events) { event ->
        when (event) {
            is StoryDetailEvent.Error -> {
                Toast.makeText(
                    context,
                    event.error.toString(context),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    Scaffold(
        modifier = modifier
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Detail Story")
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                ),
                navigationIcon = {
                    IconButton(onClick = { onAction(StoryDetailAction.OnBackClicked) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Close",
                        )
                    }
                },
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(story.photoUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "Story Image",
                modifier = Modifier
                    .aspectRatio(16 / 9f)
                    .height(224.dp)
                    .fillMaxWidth()
                    .clip(shape = MaterialTheme.shapes.medium)
                    .sharedElement(
                        state = rememberSharedContentState(key = "storyImage/${story.id}"),
                        animatedVisibilityScope = animatedVisibilityScope,
                        boundsTransform = { _, _ ->
                            tween(durationMillis = 300)
                        }
                    ),
                contentScale = ContentScale.Crop,
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .sharedElement(
                        state = rememberSharedContentState(key = "storyRow/${story.id}"),
                        animatedVisibilityScope = animatedVisibilityScope,
                        boundsTransform = { _, _ ->
                            tween(durationMillis = 300)
                        }
                    ),
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

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = story.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}