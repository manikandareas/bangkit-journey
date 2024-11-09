package com.manikandareas.stories.auth.presentation.auth_register

import com.manikandareas.stories.core.domain.validation.ValidateEmail
import com.manikandareas.stories.core.domain.validation.ValidateName
import com.manikandareas.stories.core.domain.validation.ValidatePassword
import com.manikandareas.stories.core.domain.validation.ValidateRepeatedPassword

data class AuthRegisterValidation(
    val validateEmail: ValidateEmail = ValidateEmail(),
    val validateName: ValidateName = ValidateName(),
    val validatePassword: ValidatePassword = ValidatePassword(),
    val validateRepeatedPassword: ValidateRepeatedPassword = ValidateRepeatedPassword(),
)