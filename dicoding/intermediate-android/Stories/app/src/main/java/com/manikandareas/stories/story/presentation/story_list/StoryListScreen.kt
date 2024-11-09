@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)

package com.manikandareas.stories.story.presentation.story_list

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.manikandareas.stories.R
import com.manikandareas.stories.core.presentation.components.StoriesDialog
import com.manikandareas.stories.core.presentation.util.ObserveAsEvents
import com.manikandareas.stories.core.presentation.util.toString
import com.manikandareas.stories.story.presentation.components.StoryItem
import kotlinx.coroutines.flow.Flow

@Composable
fun SharedTransitionScope.ListStoryScreen(
    state: StoryListState,
    onAction: (StoryListAction) -> Unit,
    modifier: Modifier = Modifier,
    events: Flow<StoryListEvent>,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val isDialogOpen = remember { mutableStateOf(false) }

    ObserveAsEvents(events = events) { event ->
        when (event) {
            is StoryListEvent.Error -> {
                Toast.makeText(
                    context,
                    event.error.toString(context),
                    Toast.LENGTH_SHORT
                ).show()
            }

            StoryListEvent.Logout -> {
                isDialogOpen.value = true
            }
        }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
            .background(MaterialTheme.colorScheme.background),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onAction(StoryListAction.MoveToCreateStory)
                },
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.secondary,

                ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Story"
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        topBar = {
            TopAppBar(
                title = {
                    Text("Stories")
                },
                scrollBehavior = scrollBehavior,
                actions = {
                    IconButton(onClick = {onAction(StoryListAction.OnLogoutClicked)}) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Logout"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->


        if (isDialogOpen.value) {
            StoriesDialog(
                dialogTitle = "Are you sure you want to logout?",
                onConfirmation = {
                    isDialogOpen.value = false
                    onAction(StoryListAction.OnConfirmLogoutClicked)
                },
                onDismissRequest = { isDialogOpen.value = false },
                confirmButtonText = "Logout",
                dismissButtonText = "Cancel",
                dialogText = "Your session will be closed and you will be logged out",
                icon = painterResource(id = R.drawable.ic_logout)
            )
        }



        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),

            ) {
            items(state.stories) { story ->
                StoryItem(
                    story = story,
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable {
                            onAction(StoryListAction.OnStoryListClick(story.id))
                        },
                    storyImageStyle = Modifier.sharedElement(
                        state = rememberSharedContentState(key = "storyImage/${story.id}"),
                        animatedVisibilityScope = animatedVisibilityScope,
                        boundsTransform = { _, _ ->
                            tween(durationMillis = 300)
                        }
                    ),
                    storyRowStyle = Modifier.sharedElement(
                        state = rememberSharedContentState(key = "storyRow/${story.id}"),
                        animatedVisibilityScope = animatedVisibilityScope,
                        boundsTransform = { _, _ ->
                            tween(durationMillis = 300)
                        }
                    ),
                )
            }
        }

    }

    if (state.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.6f)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp),
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

//@Preview
//@Composable
//fun PreviewListStoryScreen() {
//    StoriesTheme {
//        ListStoryScreen(
//            state = StoryListState(
//                stories = 0.rangeTo(10).map {
//                    StoryUi(
//                        id = it.toString(),
//                        name = "Story $it",
//                        photoUrl = "https://picsum.photos/200/300",
//                        description = "Description $it",
//                        createdAt = ZonedDateTime.now(),
//                        address = Address(
//                            countryName = "United States",
//                            countryCode = "US",
//                            latitude = 0.0,
//                            longitude = 0.0
//                        )
//                    )
//                }
//            ),
//            events = emptyFlow(),
//            onAction = {}
//        )
//
//
//    }
//}