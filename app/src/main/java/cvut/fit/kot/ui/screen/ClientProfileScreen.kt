package cvut.fit.kot.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import cvut.fit.kot.ui.components.AvatarImage
import cvut.fit.kot.ui.components.ContactInfoCard
import cvut.fit.kot.ui.components.LocationInfo
import cvut.fit.kot.ui.viewModel.ClientProfileViewModel
import cvut.fit.kot.ui.viewModel.ClientProfileViewModel.UiState
import cvut.fit.kot.ui.viewModel.ClientUiModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    rootNav: NavHostController,
    vm: ClientProfileViewModel = hiltViewModel()
) {
    val uiState by vm.state.collectAsState()
    val expanded = remember { mutableStateOf(false) }
    val savedStateHandle = rootNav.currentBackStackEntry?.savedStateHandle
    val showLogoutDialog = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        savedStateHandle
            ?.getStateFlow("profile_updated", false)
            ?.collect { changed ->
                if (changed) {
                    vm.load()
                    savedStateHandle["profile_updated"] = false
                }
            }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Profile") },
                actions = {
                    Box {
                        IconButton(onClick = { expanded.value = true }) {
                            Icon(Icons.Outlined.Edit, contentDescription = "Menu")
                        }

                        DropdownMenu(
                            expanded = expanded.value,
                            onDismissRequest = { expanded.value = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Edit profile") },
                                onClick = {
                                    expanded.value = false
                                    rootNav.navigate("edit_profile")
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Change password") },
                                onClick = {
                                    expanded.value = false
                                    rootNav.navigate("change_password")
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Change email") },
                                onClick = {
                                    expanded.value = false
                                    rootNav.navigate("change_email")
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Log out") },
                                onClick = {
                                    expanded.value = false
                                    showLogoutDialog.value = true
                                }
                            )
                        }
                    }
                }
            )
        }
    ) { inner ->
        Box(Modifier.padding(inner).fillMaxSize(), contentAlignment = Alignment.Center) {
            when (uiState) {
                UiState.Loading -> CircularProgressIndicator()
                is UiState.Error -> ErrorContent((uiState as UiState.Error).throwable)
                is UiState.Success -> ProfileContent((uiState as UiState.Success).user)
            }
        }
    }
    if (showLogoutDialog.value) {
        AlertDialog(
            modifier = Modifier.testTag("logout_dialog"),
            onDismissRequest = { showLogoutDialog.value = false },
            text  = { Text("Do you really want to log out of your account?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog.value = false
                        vm.logout {
                            rootNav.navigate("landing") {
                                popUpTo(0) { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    }
                ) {
                    Text("Yes", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog.value = false }) {
                    Text("No")
                }
            }
        )
    }
}

@Composable
private fun ProfileContent(ui: ClientUiModel) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.padding(horizontal = 24.dp)
    ) {
        AvatarImage(ui.avatar)

        Text(ui.name, style = MaterialTheme.typography.headlineSmall)

        LocationInfo(
            address = ui.city
        )

        AssistChip(
            onClick = { },
            label = { Text(ui.role) },
            colors = AssistChipDefaults.assistChipColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        )

        ContactInfoCard(ui.email, ui.phone)
    }
}


@Composable
private fun ErrorContent(th: Throwable) {
    Text("Error: ${th.localizedMessage}")
}

@Preview(showBackground = true, widthDp = 360)
@Composable
private fun ProfilePreview() {
    ProfileContent(
        ClientUiModel(
            avatar = null,
            name = "Steve Smith",
            city = "Prague",
            role = "CLIENT",
            email = "steve@mail.com",
            phone = "+420 123 456"
        )
    )
}
