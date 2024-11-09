package com.manikandareas.stories.di

import com.manikandareas.stories.auth.presentation.auth_login.AuthLoginViewModel
import com.manikandareas.stories.auth.presentation.auth_register.AuthRegisterViewModel
import com.manikandareas.stories.story.presentation.create_story.CreateStoryViewModel
import com.manikandareas.stories.story.presentation.story_detail.StoryDetailViewModel
import com.manikandareas.stories.story.presentation.story_list.StoryListViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module


val viewModelModule = module{
    viewModelOf(::AuthRegisterViewModel).bind<AuthRegisterViewModel>()
    viewModelOf(::AuthLoginViewModel).bind<AuthLoginViewModel>()
    viewModelOf(::StoryListViewModel).bind<StoryListViewModel>()
    viewModelOf(::CreateStoryViewModel).bind<CreateStoryViewModel>()
    viewModelOf(::StoryDetailViewModel).bind<StoryDetailViewModel>()
}