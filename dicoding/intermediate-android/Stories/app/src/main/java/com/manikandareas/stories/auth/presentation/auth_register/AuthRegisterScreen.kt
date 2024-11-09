package com.manikandareas.stories.auth.presentation.auth_register

import android.util.Log
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.manikandareas.stories.core.presentation.components.StoriesDialog
import com.manikandareas.stories.core.presentation.components.StoriesOutlinedPasswordField
import com.manikandareas.stories.core.presentation.components.StoriesOutlinedTextField
import com.manikandareas.stories.core.presentation.util.ObserveAsEvents
import com.manikandareas.stories.core.presentation.util.toString
import com.manikandareas.stories.ui.theme.StoriesTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun AuthRegisterScreen(
    state: AuthRegisterState,
    events: Flow<AuthRegisterEvent>,
    onAction: (AuthRegisterAction) -> Unit,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current
    val isDialogOpen = remember { mutableStateOf(false) }
    ObserveAsEvents(events = events) { event ->
        when (event) {
            is AuthRegisterEvent.Error -> {
                Toast.makeText(
                    context,
                    event.error.toString(context),
                    Toast.LENGTH_SHORT
                ).show()
            }

            AuthRegisterEvent.RegisterSuccess -> {
                isDialogOpen.value = true
            }
        }
    }



    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

        if (isDialogOpen.value) {
            StoriesDialog(
                dialogTitle = "Yeay! Welcome to Stories",
                onConfirmation = {
                    isDialogOpen.value = false
                    onAction(AuthRegisterAction.MoveToLogin)
                },
                onDismissRequest = { isDialogOpen.value = false },
                confirmButtonText = "Log in Now",
                dismissButtonText = "Later",
                dialogText = "You have successfully registered to Stories. Please login to continue",
                icon = painterResource(id = R.drawable.ic_waving_hand)
            )
        }
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
                text = stringResource(R.string.set_up_your_account),
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
                    value = state.name,
                    onValueChange = {
                        Log.d("AuthRegisterScreen", "Name changed to: $it")
                        onAction(AuthRegisterAction.OnNameChanged(it))
                    },
                    isError = state.nameError != null,
                    errorText = state.nameError ?: "",
                    modifier = modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text
                    ),
                    placeholder = "John Doe"
                )

                StoriesOutlinedTextField(
                    value = state.email,
                    onValueChange = { onAction(AuthRegisterAction.OnEmailChanged(it)) },
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
                    onPasswordChange = { onAction(AuthRegisterAction.OnPasswordChanged(it)) },
                    isError = state.passwordError != null,
                    errorText = state.passwordError ?: "",
                    placeholder = "Password",
                    modifier = modifier.fillMaxWidth(),
                )

                StoriesOutlinedPasswordField(
                    password = state.repeatedPassword,
                    onPasswordChange = { onAction(AuthRegisterAction.OnRepeatedPasswordChanged(it)) },
                    isError = state.repeatedPasswordError != null,
                    errorText = state.repeatedPasswordError ?: "",
                    placeholder = "Confirm Password",
                    modifier = modifier.fillMaxWidth(),
                )

                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    onClick = { onAction(AuthRegisterAction.OnRegisterClick) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !state.isLoading
                ) {
                    Text(text = "Register", modifier = Modifier.padding(vertical = 8.dp))
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HorizontalDivider(modifier = Modifier.weight(1f))
                    Text(
                        text = "Already have an account?",
                        modifier = Modifier.padding(horizontal = 16.dp),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.W400
                    )
                    HorizontalDivider(modifier = Modifier.weight(1f))
                }

                OutlinedButton(
                    enabled = !state.isLoading,
                    onClick = { onAction(AuthRegisterAction.MoveToLogin) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Login", modifier = Modifier.padding(vertical = 8.dp))
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
fun AuthRegisterScreenPreview() {
    StoriesTheme {
        AuthRegisterScreen(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background),
            state = AuthRegisterState(), onAction = {},
            events = emptyFlow()
        )
    }
}