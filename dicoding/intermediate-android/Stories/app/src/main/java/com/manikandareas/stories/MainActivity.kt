@file:OptIn(ExperimentalMaterial3Api::class)

package com.manikandareas.stories

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.manikandareas.stories.auth.data.preference.PreferenceDataSource
import com.manikandareas.stories.core.navigation.Destination
import com.manikandareas.stories.core.navigation.Navigator
import com.manikandareas.stories.core.navigation.StoriesNavigation
import com.manikandareas.stories.ui.theme.StoriesTheme
import org.koin.compose.koinInject

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StoriesTheme {
                val navController = rememberNavController()
                val navigator = koinInject<Navigator>()
                val preferenceDataSource = koinInject<PreferenceDataSource>()
                val isLoggedIn by preferenceDataSource.isLoggedIn.collectAsStateWithLifecycle(initialValue = false)

                val startDestination =
                    if (isLoggedIn) Destination.HomeGraph else Destination.AuthGraph

                StoriesNavigation(
                    navController = navController,
                    navigator = navigator,
                    startDestination = startDestination
                )
            }
        }
    }
}
