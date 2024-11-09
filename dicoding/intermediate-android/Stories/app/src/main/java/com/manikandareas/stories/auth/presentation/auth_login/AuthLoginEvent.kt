package com.manikandareas.stories.auth.presentation.auth_login

import com.manikandareas.stories.core.domain.util.NetworkError

sealed interface AuthLoginEvent {
    data class Error(val error:NetworkError): AuthLoginEvent
    object Success: AuthLoginEvent
}