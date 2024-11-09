package com.manikandareas.stories.auth.presentation.auth_login

import androidx.compose.runtime.Immutable

@Immutable
data class AuthLoginState(
    val email: String = "",
    val emailError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val isLoading: Boolean = false,
)