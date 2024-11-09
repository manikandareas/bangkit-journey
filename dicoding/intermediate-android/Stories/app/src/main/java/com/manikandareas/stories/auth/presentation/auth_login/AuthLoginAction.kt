package com.manikandareas.stories.auth.presentation.auth_login

sealed class AuthLoginAction {
    data class OnEmailChanged(val email: String) : AuthLoginAction()
    data class OnPasswordChanged(val password: String) : AuthLoginAction()

    object MoveToRegister : AuthLoginAction()
    object OnLoginClick : AuthLoginAction()
}