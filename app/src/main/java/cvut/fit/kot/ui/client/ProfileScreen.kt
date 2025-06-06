package cvut.fit.kot.ui.client

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import cvut.fit.kot.ui.client.ClientProfileViewModel.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    rootNav: NavHostController,
    viewModel: ClientProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.state.collectAsState()
    val expanded = remember { mutableStateOf(false) }

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
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Logout") },
                                onClick = {
                                    expanded.value = false
                                    viewModel.logout {
                                        rootNav.navigate("landing") {
                                            popUpTo(0) { inclusive = true }
                                            launchSingleTop = true
                                        }
                                    }
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
}

/* --- success layout extracted for reuse & preview --- */
@Composable
private fun ProfileContent(ui: ClientUiModel) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.padding(horizontal = 24.dp)
    ) {
        ui.avatar?.let { bitmap ->
            Image(
                bitmap = bitmap,
                contentDescription = null,
                modifier = Modifier
                    .size(112.dp)
                    .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                    .padding(2.dp)
                    .clip(CircleShape)
            )
        } ?: Icon(
            imageVector = Icons.Default.AccountCircle,
            contentDescription = null,
            modifier = Modifier
                .size(112.dp)
                .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                .padding(2.dp)
                .clip(CircleShape)
        )

        Text(ui.name, style = MaterialTheme.typography.headlineSmall)
        Text("📍 ${ui.city}", style = MaterialTheme.typography.bodyMedium)

        AssistChip(
            onClick = { },
            label = { Text(ui.role) },
            colors = AssistChipDefaults.assistChipColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        )

        ElevatedCard(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text("Contacts", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(4.dp))
                Text("Email:  ${ui.email}")
                Text("Phone:  ${ui.phone}")
            }
        }
    }
}


@Composable
private fun ErrorContent(th: Throwable) {
    Text("Error: ${th.localizedMessage}")
}

/* --- static preview with fake data --- */
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
