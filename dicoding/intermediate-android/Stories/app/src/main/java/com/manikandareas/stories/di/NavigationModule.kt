package com.manikandareas.stories.di

import com.manikandareas.stories.core.navigation.DefaultNavigator
import com.manikandareas.stories.core.navigation.Destination
import com.manikandareas.stories.core.navigation.Navigator
import org.koin.dsl.module

val navigationModule= module{
    single<Navigator> {
        DefaultNavigator(startDestination = Destination.HomeGraph)
    }
}