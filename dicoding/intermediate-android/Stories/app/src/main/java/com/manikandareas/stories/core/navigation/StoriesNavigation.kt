@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.manikandareas.stories.core.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.manikandareas.stories.auth.presentation.auth_login.AuthLoginScreen
import com.manikandareas.stories.auth.presentation.auth_login.AuthLoginViewModel
import com.manikandareas.stories.auth.presentation.auth_register.AuthRegisterScreen
import com.manikandareas.stories.auth.presentation.auth_register.AuthRegisterViewModel
import com.manikandareas.stories.core.presentation.util.ObserveAsEvents
import com.manikandareas.stories.story.presentation.create_story.CreateStoryScreen
import com.manikandareas.stories.story.presentation.create_story.CreateStoryViewModel
import com.manikandareas.stories.story.presentation.story_detail.StoryDetailScreen
import com.manikandareas.stories.story.presentation.story_detail.StoryDetailViewModel
import com.manikandareas.stories.story.presentation.story_list.ListStoryScreen
import com.manikandareas.stories.story.presentation.story_list.StoryListViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf


@Composable
fun StoriesNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: Destination,
    navigator: Navigator,

    ) {

    ObserveAsEvents(events = navigator.navigationActions) { action ->
        when (action) {
            is NavigationAction.Navigate -> navController.navigate(
                action.destination
            ) {
                action.navOptions(this)
            }

            NavigationAction.NavigateUp -> navController.navigateUp()
        }
    }
    SharedTransitionLayout {
        NavHost(
            navController = navController,
            startDestination = startDestination
        ) {
            navigation<Destination.AuthGraph>(
                startDestination = Destination.RegisterScreen
            ) {
                composable<Destination.RegisterScreen> {
                    val viewModel = koinViewModel<AuthRegisterViewModel>()
                    AuthRegisterScreen(
                        state = viewModel.state,
                        onAction = viewModel::onAction,
                        modifier = modifier,
                        events = viewModel.events
                    )
                }
                composable<Destination.LoginScreen> {
                    val viewModel = koinViewModel<AuthLoginViewModel>()
                    AuthLoginScreen(
                        state = viewModel.state,
                        onAction = viewModel::onAction,
                        modifier = modifier,
                        events = viewModel.events
                    )
                }
            }


            navigation<Destination.HomeGraph>(
                startDestination = Destination.ListStoryScreen
            ) {

                composable<Destination.ListStoryScreen> {
                    val viewModel = koinViewModel<StoryListViewModel>()
                    val state by viewModel.state.collectAsStateWithLifecycle()
                    ListStoryScreen(
                        state = state,
                        onAction = viewModel::onAction,
                        modifier = modifier,
                        events = viewModel.events,
                        animatedVisibilityScope = this
                    )
                }
                composable<Destination.AddStoryScreen> {
                    val viewModel = koinViewModel<CreateStoryViewModel>()
                    CreateStoryScreen(
                        state = viewModel.state,
                        onAction = viewModel::onAction,
                        modifier = modifier,
                        events = viewModel.events
                    )
                }
                composable<Destination.StoryDetailScreen> {
                    val args = it.toRoute<Destination.StoryDetailScreen>()
                    val viewModel: StoryDetailViewModel =
                        koinViewModel { parametersOf(args.storyId) }
                    val state by viewModel.state.collectAsStateWithLifecycle()
                    StoryDetailScreen(
                        state = state,
                        modifier = modifier,
                        animatedVisibilityScope = this,
                        events = viewModel.events,
                        onAction = viewModel::onAction
                    )
                }
            }
        }
    }
}

