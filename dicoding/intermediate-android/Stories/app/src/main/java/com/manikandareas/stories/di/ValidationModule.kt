package com.manikandareas.stories.di

import com.manikandareas.stories.auth.presentation.auth_login.AuthLoginValidation
import com.manikandareas.stories.auth.presentation.auth_register.AuthRegisterValidation
import org.koin.dsl.module

val validationModule = module {
    single { AuthRegisterValidation() }
    single { AuthLoginValidation() }
}