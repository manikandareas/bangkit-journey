package com.manikandareas.stories.auth.presentation.auth_register

import com.manikandareas.stories.core.domain.util.NetworkError

sealed interface AuthRegisterEvent {
    data class Error(val error:NetworkError): AuthRegisterEvent
    object RegisterSuccess: AuthRegisterEvent
}