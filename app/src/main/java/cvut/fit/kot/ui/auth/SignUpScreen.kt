package cvut.fit.kot.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cvut.fit.kot.ui.theme.MyApplicationTheme

@Composable
fun SignUpScreen(
    uiState: AuthViewModel.UiState,
    onSignUp: (String, String, String) -> Unit,
    onErrorShown: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("CLIENT") }

    if (uiState is AuthViewModel.UiState.Error) {
        AlertDialog(
            onDismissRequest = onErrorShown,
            confirmButton = { TextButton(onClick = onErrorShown) { Text("OK") } },
            title = { Text("Error") },
            text  = { Text(uiState.message) }
        )
    }

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

        RoleSelector(selectedRole = role, onRoleSelected = { role = it })
        Spacer(Modifier.height(16.dp))

        Button(
            onClick = { onSignUp(email, password, role) },
            enabled = uiState !is AuthViewModel.UiState.Loading,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            if (uiState is AuthViewModel.UiState.Loading) {
                CircularProgressIndicator(strokeWidth = 2.dp, modifier = Modifier.size(24.dp))
            } else {
                Text("Sign Up")
            }
        }

        Spacer(Modifier.height(16.dp))

        OutlinedButton(
            onClick = onBack,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) { Text("Back") }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewSignUp() {
    MyApplicationTheme {
        SignUpScreen(
            uiState = AuthViewModel.UiState.Idle,
            onSignUp = { _, _, _ -> },
            onErrorShown = {},
            onBack = {}
        )
    }
}
