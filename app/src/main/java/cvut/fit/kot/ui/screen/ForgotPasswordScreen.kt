package cvut.fit.kot.ui.screen

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import cvut.fit.kot.ui.components.BackButton
import cvut.fit.kot.ui.components.TextInputField
import cvut.fit.kot.ui.viewModel.ForgotPasswordViewModel
import cvut.fit.kot.ui.components.CenterProgress

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
    rootNav: NavHostController,
    vm: ForgotPasswordViewModel = hiltViewModel()
) {
    val st by vm.state.collectAsState()
    val snackbar = remember { SnackbarHostState() }

    LaunchedEffect(st.success) {
        if (st.success) {
            val encoded = Uri.encode(st.email)

            rootNav.navigate("reset_password?email=$encoded") {
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
                    BackButton(navController = rootNav)
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

                TextInputField(
                    value = st.email,
                    onValueChange = vm::setEmail,
                    label = "Email",
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}