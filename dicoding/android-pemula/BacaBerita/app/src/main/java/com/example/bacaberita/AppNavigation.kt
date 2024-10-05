package com.example.bacaberita

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavigation(newsViewModel: NewsViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Route.HOME_PAGE, builder = {
        composable(Route.HOME_PAGE) {
            HomePage(navController, newsViewModel)
        }
        composable(Route.ABOUT_PAGE) {
            AboutPage(navController)
        }
        composable(Route.DETAIL_NEWS_PAGE) {
            DetailNewsPage(navController, newsViewModel)
        }
    })
}


object Route {
    const val HOME_PAGE = "HomePage"
    const val ABOUT_PAGE = "AboutPage"
    const val DETAIL_NEWS_PAGE = "DetailNewsPage"
}