package com.manikandareas.stories.core.domain.validation

import android.util.Patterns

class ValidateName {
    fun execute(name: String): ValidationResult {
        if(name.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "The name can't be blank"
            )
        }
        if(name.length < 3) {
            return ValidationResult(
                successful = false,
                errorMessage = "The name must be at least 3 characters long"
            )
        }
        return ValidationResult(
            successful = true
        )
    }
}