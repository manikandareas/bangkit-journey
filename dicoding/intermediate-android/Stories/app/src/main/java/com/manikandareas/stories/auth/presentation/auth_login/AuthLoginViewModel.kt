package com.manikandareas.stories.auth.presentation.auth_login

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manikandareas.stories.auth.data.networking.dto.LoginRequestDto
import com.manikandareas.stories.auth.data.preference.PreferenceDataSource
import com.manikandareas.stories.auth.domain.AuthDataSource
import com.manikandareas.stories.core.domain.util.onError
import com.manikandareas.stories.core.domain.util.onSuccess
import kotlinx.coroutines.launch
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import com.manikandareas.stories.core.navigation.Destination
import com.manikandareas.stories.core.navigation.Navigator
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

class AuthLoginViewModel(
    private val authDataSource: AuthDataSource,
    private val preferenceDataSource: PreferenceDataSource,
    private val loginValidation: AuthLoginValidation,
    private val navigator: Navigator
) : ViewModel() {

    var state by mutableStateOf(AuthLoginState())
        private set

    private val _events = Channel<AuthLoginEvent>()
    val events = _events.receiveAsFlow()

    fun onAction(action: AuthLoginAction) {
        when (action) {
            is AuthLoginAction.OnEmailChanged -> {
                state = state.copy(email = action.email, emailError = null)
                val result = loginValidation.validateEmail.execute(state.email)
                if (!result.successful) {
                    state = state.copy(emailError = result.errorMessage)
                }
            }

            is AuthLoginAction.OnPasswordChanged -> {
                state = state.copy(password = action.password, passwordError = null)
                val isFormValid = action.password.isNotEmpty()
                if (!isFormValid) {
                    state = state.copy(passwordError = "Password is required")
                }
            }

            is AuthLoginAction.OnLoginClick -> {
                onSubmit()
            }

            AuthLoginAction.MoveToRegister -> {
                viewModelScope.launch {
                    navigator.navigate(Destination.RegisterScreen)
                }
            }
        }
    }

    private fun onSubmit() = viewModelScope.launch {
        state = state.copy(isLoading = true)
        authDataSource.login(
            LoginRequestDto(
                email = state.email,
                password = state.password
            )
        ).onSuccess { response ->
            state = state.copy(isLoading = false)
            _events.send(AuthLoginEvent.Success)
            preferenceDataSource.setSessionToken(response.loginResult.token)
            navigator.navigate(Destination.HomeGraph)
        }.onError {
            state = state.copy(isLoading = false)
            _events.send(AuthLoginEvent.Error(it))
        }
    }

}