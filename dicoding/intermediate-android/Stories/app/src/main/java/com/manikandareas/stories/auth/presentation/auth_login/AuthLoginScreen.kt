package com.manikandareas.stories.auth.presentation.auth_login

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.manikandareas.stories.R
import com.manikandareas.stories.core.presentation.components.StoriesOutlinedPasswordField
import com.manikandareas.stories.core.presentation.components.StoriesOutlinedTextField
import com.manikandareas.stories.core.presentation.util.ObserveAsEvents
import com.manikandareas.stories.core.presentation.util.toString
import com.manikandareas.stories.ui.theme.StoriesTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun AuthLoginScreen(
    state: AuthLoginState,
    onAction: (AuthLoginAction) -> Unit,
    events: Flow<AuthLoginEvent>,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current
    ObserveAsEvents(events = events) { event ->
        when (event) {
            is AuthLoginEvent.Error -> {
                Toast.makeText(
                    context,
                    event.error.toString(context),
                    Toast.LENGTH_SHORT
                ).show()
            }

            AuthLoginEvent.Success -> {
                Toast.makeText(
                    context,
                    "Login successful",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {
            Image(
                painter = painterResource(id = R.drawable.social_image),
                contentDescription = stringResource(R.string.hands_connect),
            )

            Spacer(modifier = modifier.height(32.dp))

            Text(
                modifier = modifier.fillMaxWidth(),
                text = stringResource(R.string.let_s_connect_with_us),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.5.sp
            )
            Spacer(modifier = modifier.height(32.dp))

            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {


                StoriesOutlinedTextField(
                    value = state.email,
                    onValueChange = { onAction(AuthLoginAction.OnEmailChanged(it)) },
                    isError = state.emailError != null,
                    errorText = state.emailError ?: "",
                    placeholder = "Email",
                    modifier = modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email
                    ),
                )

                StoriesOutlinedPasswordField(
                    password = state.password,
                    onPasswordChange = { onAction(AuthLoginAction.OnPasswordChanged(it)) },
                    isError = state.passwordError != null,
                    errorText = state.passwordError ?: "",
                    placeholder = "Password",
                    modifier = modifier.fillMaxWidth(),
                )


                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    onClick = { onAction(AuthLoginAction.OnLoginClick) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !state.isLoading
                ) {
                    Text(text = "Login", modifier = Modifier.padding(vertical = 8.dp))
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HorizontalDivider(modifier = Modifier.weight(1f))
                    Text(
                        text = "Don't have an account?",
                        modifier = Modifier.padding(horizontal = 16.dp),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.W400
                    )
                    HorizontalDivider(modifier = Modifier.weight(1f))
                }

                OutlinedButton(
                    onClick = { onAction(AuthLoginAction.MoveToRegister) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Register", modifier = Modifier.padding(vertical = 8.dp))
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.6f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(48.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Preview
@Composable
private fun AuthLoginScreenPreview() {
    StoriesTheme {
        AuthLoginScreen(
            state = AuthLoginState(),
            onAction = {},
            events = emptyFlow()
        )
    }
}