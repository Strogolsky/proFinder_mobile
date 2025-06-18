package cvut.fit.kot.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import cvut.fit.kot.ui.viewModel.ResetPasswordViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResetPasswordScreen(
    nav: NavHostController,
    vm: ResetPasswordViewModel = hiltViewModel()
) {
    val st by vm.state.collectAsState()
    val snackbar = remember { SnackbarHostState() }

    LaunchedEffect(st.success) {
        if (st.success) {
            snackbar.showSnackbar("Password reset successfully")
            nav.popBackStack()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbar) },
        topBar = {
            TopAppBar(
                title = { Text("Reset password") },
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = null)
                    }
                },
                actions = {
                    TextButton(
                        enabled = !st.loading
                                && st.code.isNotBlank()
                                && st.new.isNotBlank()
                                && st.confirm.isNotBlank(),
                        onClick = { vm.save { } }
                    ) { Text("Save") }
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
                    value = st.code,
                    onValueChange = vm::setCode,
                    label = { Text("Verification code") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = st.new,
                    onValueChange = vm::setNew,
                    label = { Text("New password") },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = st.confirm,
                    onValueChange = vm::setConfirm,
                    label = { Text("Confirm password") },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
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