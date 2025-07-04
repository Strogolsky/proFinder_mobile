package cvut.fit.kot.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import cvut.fit.kot.data.model.OrderDto
import cvut.fit.kot.ui.components.BackButton
import cvut.fit.kot.ui.components.LocationInfo
import cvut.fit.kot.ui.components.OrderStatusChip
import cvut.fit.kot.ui.components.ServicesCard
import cvut.fit.kot.ui.viewModel.OrderDetailsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailsScreen(
    orderId: Long,
    rootNav: NavHostController,
    viewModel: OrderDetailsViewModel = hiltViewModel()
) {
    LaunchedEffect(orderId) { viewModel.load(orderId) }
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = { BackButton(rootNav) }
            )
        }
    ) { inner ->
        Box(Modifier.padding(inner)) {
            when (val s = state) {
                OrderDetailsViewModel.State.Loading   -> Loading()
                is OrderDetailsViewModel.State.Error   -> ErrorText(s.msg)
                is OrderDetailsViewModel.State.Success -> DetailsContent(s.order)
            }
        }
    }
}

@Composable private fun Loading() = Box(
    Modifier.fillMaxSize(), Alignment.Center
) { CircularProgressIndicator() }

@Composable private fun ErrorText(text: String) = Box(
    Modifier.fillMaxSize(), Alignment.Center
) { Text("Error: $text", color = MaterialTheme.colorScheme.error) }

@Composable
private fun DetailsContent(order: OrderDto) = Column(
    Modifier
        .fillMaxWidth()
        .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp)
) {
    ElevatedCard(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Text("Description", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(4.dp))
            Text(order.description.orEmpty(), style = MaterialTheme.typography.bodyLarge)
        }
    }

    ElevatedCard(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            LabeledText("Price", order.price.toString())
            OrderStatusChip(status = order.status)
            LocationInfo(address = order.location.name)
            LabeledText("Created at", order.createAt.orEmpty())
        }
    }

    ServicesCard(services = order.serviceOfferings.mapNotNull { it.name })
}

@Composable
private fun LabeledText(label: String, value: String) = Row {
    Text("$label: ", style = MaterialTheme.typography.bodyMedium)
    Text(value, style = MaterialTheme.typography.bodyMedium)
}



