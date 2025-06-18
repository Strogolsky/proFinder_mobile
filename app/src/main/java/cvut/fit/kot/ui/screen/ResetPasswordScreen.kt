package cvut.fit.kot.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import cvut.fit.kot.ui.components.BackButton
import cvut.fit.kot.ui.components.NumberInputField
import cvut.fit.kot.ui.components.PasswordInputField
import cvut.fit.kot.ui.viewModel.ResetPasswordViewModel
import cvut.fit.kot.ui.components.CenterProgress

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResetPasswordScreen(
    rootNav: NavHostController,
    vm: ResetPasswordViewModel = hiltViewModel()
) {
    val st by vm.state.collectAsState()
    val snackbar = remember { SnackbarHostState() }

    LaunchedEffect(st.success) {
        if (st.success) {
            snackbar.showSnackbar("Password reset successfully")
            rootNav.popBackStack()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbar) },
        topBar = {
            TopAppBar(
                title = { Text("Reset password") },
                navigationIcon = {
                    BackButton(navController = rootNav)
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

                NumberInputField(
                    value = st.code,
                    onValueChange = vm::setCode,
                    label = "Verification code",
                    modifier = Modifier.fillMaxWidth()
                )

                PasswordInputField(
                    value = st.new,
                    onValueChange = vm::setNew,
                    label = "New password",
                    modifier = Modifier.fillMaxWidth()
                )

                PasswordInputField(
                    value = st.confirm,
                    onValueChange = vm::setConfirm,
                    label = "Confirm password",
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}