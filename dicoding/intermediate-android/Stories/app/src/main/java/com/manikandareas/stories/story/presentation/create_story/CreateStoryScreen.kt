@file:OptIn(ExperimentalMaterial3Api::class)

package com.manikandareas.stories.story.presentation.create_story

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.manikandareas.stories.R
import com.manikandareas.stories.core.presentation.components.StoriesDialog
import com.manikandareas.stories.core.presentation.components.StoriesOutlineBox
import com.manikandareas.stories.core.presentation.util.ImageUtils
import com.manikandareas.stories.core.presentation.util.ObserveAsEvents
import com.manikandareas.stories.core.presentation.util.toString
import com.manikandareas.stories.ui.theme.StoriesTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun CreateStoryScreen(
    state: CreateStoryState,
    onAction: (CreateStoryAction) -> Unit,
    events: Flow<CreateStoryEvent>,
    modifier: Modifier = Modifier,
) {

    val context = LocalContext.current

    val isDialogOpen = remember { mutableStateOf(false) }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()


    val openGalleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                onAction(CreateStoryAction.OnGallerySelected(it))
            }
        }

    val openCameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
            bitmap?.let {
                val imageUri = ImageUtils.saveBitmapToFile(context, bitmap)
                onAction(CreateStoryAction.OnCameraSelected(imageUri))
            }
        }


    ObserveAsEvents(events = events) { event ->
        when (event) {
            is CreateStoryEvent.Error -> {
                Toast.makeText(
                    context, event.error.toString(context), Toast.LENGTH_SHORT
                ).show()
            }

            CreateStoryEvent.Success -> {
                Toast.makeText(
                    context, "Story created successfully", Toast.LENGTH_SHORT
                ).show()
                isDialogOpen.value = true
            }

            CreateStoryEvent.OpenCamera -> {
                openCameraLauncher.launch(null)
            }

            CreateStoryEvent.OpenGallery -> {
                openGalleryLauncher.launch("image/*")
            }
        }
    }

    Scaffold(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Create Story")
                },
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    IconButton(onClick = { onAction(CreateStoryAction.MoveToStoryList) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Close",
                        )
                    }
                },
            )
        }
    ) { innerPadding ->

        if (isDialogOpen.value) {
            StoriesDialog(
                dialogTitle = "Your story has been created!",
                onConfirmation = {
                    isDialogOpen.value = false
                    onAction(CreateStoryAction.MoveToStoryList)
                },
                onDismissRequest = {
                    onAction(CreateStoryAction.OnCreateAnotherStoryClicked)
                    isDialogOpen.value = false
                },
                confirmButtonText = "Continue to home",
                dismissButtonText = "Create another story",
                dialogText = "Your story has been created successfully",
                icon = painterResource(id = R.drawable.ic_waving_hand)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            if (state.imageUri != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(MaterialTheme.shapes.medium),
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(state.imageUri),
                        contentDescription = stringResource(R.string.story_image),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(MaterialTheme.shapes.medium),
                        contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                    )

                    IconButton(
                        onClick = { onAction(CreateStoryAction.OnXClicked) },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .background(MaterialTheme.colorScheme.surface)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = stringResource(R.string.camera),
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                }
            } else {
                StoriesOutlineBox(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    borderColor = MaterialTheme.colorScheme.onSurface,
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { onAction(CreateStoryAction.OnGalleryClicked) }) {
                            Icon(
                                painter = painterResource(R.drawable.ic_gallery),
                                contentDescription = stringResource(R.string.gallery),
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.size(48.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        IconButton(
                            onClick = { onAction(CreateStoryAction.OnCameraClicked) },
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_camera),
                                contentDescription = stringResource(R.string.camera),
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.size(48.dp)
                            )
                        }
                    }
                }
            }

            TextField(
                value = state.description,
                onValueChange = { onAction(CreateStoryAction.OnDescriptionChanged(it)) },
                label = { Text("Description") },
                modifier = Modifier
                    .height(150.dp)
                    .fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    focusedLabelColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    cursorColor = MaterialTheme.colorScheme.primary,
                )
            )

            Button(
                enabled = !state.isLoading && state.description.isNotBlank() && state.imageUri != null,
                onClick = { onAction(CreateStoryAction.OnCreateStoryClicked(context)) },
            ) {
                Text("Create Story")
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

@Preview
@Composable
fun CreateStoryScreenPreview() {
    StoriesTheme {
        CreateStoryScreen(
            state = CreateStoryState(), onAction = {}, events = emptyFlow()
        )
    }
}