package com.manikandareas.stories.auth.presentation.auth_register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manikandareas.stories.auth.data.networking.dto.RegisterRequestDto
import com.manikandareas.stories.auth.domain.AuthDataSource
import com.manikandareas.stories.core.domain.util.NetworkError
import com.manikandareas.stories.core.domain.util.onError
import com.manikandareas.stories.core.domain.util.onSuccess
import com.manikandareas.stories.core.navigation.Destination
import com.manikandareas.stories.core.navigation.Navigator
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class AuthRegisterViewModel(
    private val authDataSource: AuthDataSource,
    private val registerValidation: AuthRegisterValidation,
    private val navigator: Navigator
) : ViewModel() {

    var state by mutableStateOf(AuthRegisterState())
        private set

    private val _events = Channel<AuthRegisterEvent>()
    val events = _events.receiveAsFlow()

    fun onAction(action: AuthRegisterAction) {
        when (action) {
            is AuthRegisterAction.OnEmailChanged -> {
                state = state.copy(email = action.email, emailError = null)
                val result = registerValidation.validateEmail.execute(state.email)
                if (!result.successful) state = state.copy(emailError = result.errorMessage)
            }

            is AuthRegisterAction.OnPasswordChanged -> {
                state = state.copy(password = action.password, passwordError = null)
                val result = registerValidation.validatePassword.execute(state.password)
                if (!result.successful) state = state.copy(passwordError = result.errorMessage)
            }

            is AuthRegisterAction.OnRepeatedPasswordChanged -> {
                state = state.copy(
                    repeatedPassword = action.repeatedPassword,
                    repeatedPasswordError = null
                )
                val result = registerValidation.validateRepeatedPassword.execute(
                    state.password,
                    state.repeatedPassword
                )
                if (!result.successful) state =
                    state.copy(repeatedPasswordError = result.errorMessage)
            }

            is AuthRegisterAction.OnNameChanged -> {
                state = state.copy(name = action.name, nameError = null)
                val result = registerValidation.validateName.execute(state.name)
                if (!result.successful) state = state.copy(nameError = result.errorMessage)
            }

            is AuthRegisterAction.OnRegisterClick -> {
                onSubmit()
            }

            AuthRegisterAction.MoveToLogin -> {
                viewModelScope.launch {
                    navigator.navigate(Destination.LoginScreen)
                }
            }
        }
    }

    private fun onSubmit() {
        viewModelScope.launch {
            try {
                val nameResult = registerValidation.validateName.execute(state.name)
                val emailResult = registerValidation.validateEmail.execute(state.email)
                val passwordResult = registerValidation.validatePassword.execute(state.password)
                val repeatedPasswordResult = registerValidation.validateRepeatedPassword.execute(
                    state.password,
                    state.repeatedPassword
                )

                val hasError = listOf(
                    nameResult,
                    emailResult,
                    passwordResult,
                    repeatedPasswordResult
                ).any { !it.successful }

                if (hasError) {
                    state = state.copy(
                        nameError = nameResult.errorMessage,
                        emailError = emailResult.errorMessage,
                        passwordError = passwordResult.errorMessage,
                        repeatedPasswordError = repeatedPasswordResult.errorMessage
                    )
                    return@launch
                }

                state = state.copy(isLoading = true)

                authDataSource.register(
                    RegisterRequestDto(
                        name = state.name,
                        email = state.email,
                        password = state.password
                    )
                ).onSuccess {
                    state = state.copy(
                        isLoading = false,
                        name = "",
                        email = "",
                        password = "",
                        repeatedPassword = ""
                    )
                    _events.send(AuthRegisterEvent.RegisterSuccess)
                }.onError { error ->
                    state = state.copy(isLoading = false)
                    _events.send(AuthRegisterEvent.Error(error))
                }
            } catch (e: Exception) {
                state = state.copy(isLoading = false)
                _events.send(AuthRegisterEvent.Error(NetworkError.UNKNOWN))
            }
        }
    }
}