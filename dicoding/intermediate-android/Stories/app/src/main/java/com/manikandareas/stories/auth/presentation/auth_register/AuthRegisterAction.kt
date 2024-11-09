package com.manikandareas.stories.auth.presentation.auth_register

sealed class AuthRegisterAction {
    data class OnNameChanged(val name: String) : AuthRegisterAction()
    data class OnEmailChanged(val email: String) : AuthRegisterAction()
    data class OnPasswordChanged(val password: String) : AuthRegisterAction()
    data class OnRepeatedPasswordChanged(val repeatedPassword: String) : AuthRegisterAction()

    object MoveToLogin : AuthRegisterAction()
    object OnRegisterClick : AuthRegisterAction()
}