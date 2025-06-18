package cvut.fit.kot.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import cvut.fit.kot.ui.viewModel.SpecialistUiModel
import cvut.fit.kot.ui.viewModel.SpecialistViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpecialistScreen(
    rootNav: NavHostController,
    vm: SpecialistViewModel = hiltViewModel()
) {
    val uiState by vm.state.collectAsState()

    Scaffold(
        topBar = { CenterAlignedTopAppBar(
            title = { Text("Specialist") },

            navigationIcon = {
                IconButton(onClick = { rootNav.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Outlined.ArrowBack, null)
                }
            }
        )
                 },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Message") },
                icon = { Icon(Icons.Default.Chat, contentDescription = null) },
                onClick = { vm.openChat { chat -> rootNav.navigate("chat/$chat") } }
            )
        }
    ) { inner ->
        Box(
            modifier = Modifier
                .padding(inner)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when (uiState) {
                SpecialistViewModel.UiState.Loading ->
                    CircularProgressIndicator()

                is SpecialistViewModel.UiState.Error ->
                    Text("Error: ${(uiState as SpecialistViewModel.UiState.Error).throwable.localizedMessage}")

                is SpecialistViewModel.UiState.Success ->
                    SpecialistContent((uiState as SpecialistViewModel.UiState.Success).data)

                else -> {}
            }
        }
    }
}

@Composable
private fun SpecialistContent(ui: SpecialistUiModel) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.padding(horizontal = 24.dp)
    ) {
        ui.avatar?.let {
            Image(
                bitmap = it,
                contentDescription = null,
                modifier = Modifier
                    .size(112.dp)
                    .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                    .padding(2.dp)
                    .clip(CircleShape)
            )
        } ?: Icon(
            Icons.Default.AccountCircle,
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
            label = { Text("⭐ ${ui.rating}") },
            colors = AssistChipDefaults.assistChipColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        )

        ElevatedCard(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text("Services", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(4.dp))
                ui.services.forEach { Text("• $it") }
            }
        }

        ElevatedCard(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text("About", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(4.dp))
                Text(ui.description)
                Spacer(Modifier.height(8.dp))
                Text("Email:  ${ui.email}")
                Text("Phone:  ${ui.phone}")
            }
        }

        Spacer(Modifier.height(72.dp))
    }
}