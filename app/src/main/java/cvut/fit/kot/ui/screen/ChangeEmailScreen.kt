package cvut.fit.kot.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import cvut.fit.kot.ui.components.BackButton
import cvut.fit.kot.ui.viewModel.ChangeEmailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangeEmailScreen(
    rootNav: NavHostController,
    vm: ChangeEmailViewModel = hiltViewModel()
) {
    val st by vm.state.collectAsState()
    val snackbar = remember { SnackbarHostState() }

    LaunchedEffect(st.success) {
        if (st.success) {
            snackbar.showSnackbar("Email changed successfully")
            rootNav.popBackStack()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbar) },
        topBar = {
            TopAppBar(
                title = { Text("Change email") },
                navigationIcon = {
                    BackButton(navController = rootNav)
                },
                actions = {
                    TextButton(
                        enabled = !st.loading
                                && st.email.isNotBlank()
                                && st.password.isNotBlank(),
                        onClick = {
                            vm.save {
                                rootNav.previousBackStackEntry
                                    ?.savedStateHandle
                                    ?.set("profile_updated", true)

                                rootNav.popBackStack()
                            }
                        }
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
                        text = "Error: ${st.error}",
                        color = MaterialTheme.colorScheme.error
                    )
                }

                OutlinedTextField(
                    value = st.email,
                    onValueChange = vm::setEmail,
                    label = { Text("New email") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = st.password,
                    onValueChange = vm::setPassword,
                    label = { Text("Current password") },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable private fun CenterProgress(pad: PaddingValues) =
    Box(
        Modifier
            .fillMaxSize()
            .padding(pad),
        Alignment.Center
    ) { CircularProgressIndicator() }
