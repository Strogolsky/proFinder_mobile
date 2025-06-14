package cvut.fit.kot.ui.auth

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
    nav: NavHostController,
    vm: ForgotPasswordViewModel = hiltViewModel()
) {
    val st by vm.state.collectAsState()
    val snackbar = remember { SnackbarHostState() }

    LaunchedEffect(st.success) {
        if (st.success) {
            val encoded = Uri.encode(st.email)

            nav.navigate("reset_password?email=$encoded") {
                popUpTo("forgot_password") { inclusive = true }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbar) },
        topBar = {
            TopAppBar(
                title = { Text("Forgot password") },
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = null)
                    }
                },
                actions = {
                    TextButton(
                        enabled = !st.loading && st.email.isNotBlank(),
                        onClick = { vm.send { } }
                    ) { Text("Continue") }
                }
            )
        }
    ) { pad ->
        when {
            st.loading -> CenterProgress(pad)
            else -> Column(
                Modifier
                    .padding(pad)
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                if (st.error != null) {
                    Text(
                        text = "Error: ${'$'}{st.error}",
                        color = MaterialTheme.colorScheme.error
                    )
                }

                OutlinedTextField(
                    value = st.email,
                    onValueChange = vm::setEmail,
                    label = { Text("Email") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun CenterProgress(pad: PaddingValues) =
    Box(
        Modifier
            .fillMaxSize()
            .padding(pad),
        Alignment.Center
    ) { CircularProgressIndicator() }