package cvut.fit.kot.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import cvut.fit.kot.ui.components.AvatarImage
import cvut.fit.kot.ui.components.BackButton
import cvut.fit.kot.ui.components.ContactInfoCard
import cvut.fit.kot.ui.components.DescriptionCard
import cvut.fit.kot.ui.components.LocationInfo
import cvut.fit.kot.ui.components.ServicesCard
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
                BackButton(navController = rootNav)
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
        AvatarImage(ui.avatar)

        Text(ui.name, style = MaterialTheme.typography.headlineSmall)

        LocationInfo(
            address = ui.city
        )

        AssistChip(
            onClick = { },
            label = { Text("⭐ ${ui.rating}") },
            colors = AssistChipDefaults.assistChipColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        )

        DescriptionCard(text = ui.description)
        ContactInfoCard(ui.email, ui.phone)

        ServicesCard(
            services = ui.services,
            modifier = Modifier.fillMaxWidth()
        )
    }
}