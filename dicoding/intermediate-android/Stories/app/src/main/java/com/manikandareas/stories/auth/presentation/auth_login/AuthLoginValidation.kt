package com.manikandareas.stories.auth.presentation.auth_login

import com.manikandareas.stories.core.domain.validation.ValidateEmail
import com.manikandareas.stories.core.domain.validation.ValidatePassword

data class AuthLoginValidation(
    val validateEmail: ValidateEmail = ValidateEmail(),
    val validatePassword: ValidatePassword = ValidatePassword(),
)