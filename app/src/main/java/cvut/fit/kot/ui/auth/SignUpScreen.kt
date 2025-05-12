package cvut.fit.kot.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import cvut.fit.kot.ui.theme.MyApplicationTheme

@Composable
fun SignUpScreen(
    uiState: AuthViewModel.UiState,
    onSignUp: (email: String, password: String, role: String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf("CLIENT") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))
        RoleSelector(
            selectedRole = selectedRole,
            onRoleSelected = { selectedRole = it }
        )
        Spacer(Modifier.height(16.dp))

        when (uiState) {
            is AuthViewModel.UiState.Loading -> CircularProgressIndicator()
            is AuthViewModel.UiState.Error -> Text(
                text = (uiState as AuthViewModel.UiState.Error).message,
                color = MaterialTheme.colorScheme.error
            )
            else -> Button(
                onClick = { onSignUp(email, password, selectedRole) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Sign Up")
            }
        }

        Spacer(Modifier.height(16.dp))
        OutlinedButton(
            onClick = onBack,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text("Back")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {
    MyApplicationTheme {
        SignUpScreen(
            uiState  = AuthViewModel.UiState.Idle,
            onSignUp = { _, _, _ -> },
            onBack   = {},
            modifier  = Modifier.fillMaxSize()
        )
    }
}