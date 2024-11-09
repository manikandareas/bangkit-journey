package com.manikandareas.stories.auth.presentation.auth_register

import androidx.compose.runtime.Immutable

@Immutable
data class AuthRegisterState(
    val name: String = "",
    val nameError: String? = null,
    val email: String = "",
    val emailError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val repeatedPassword: String = "",
    val repeatedPasswordError: String? = null,
    val isLoading: Boolean = false,
)