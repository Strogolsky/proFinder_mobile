package cvut.fit.kot.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import cvut.fit.kot.data.model.*
import cvut.fit.kot.ui.components.*
import cvut.fit.kot.ui.viewModel.OrderDetailsViewModel
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailsScreen(
    orderId: Long,
    rootNav: NavHostController,
    viewModel: OrderDetailsViewModel = hiltViewModel()
) {
    LaunchedEffect(orderId) { viewModel.load(orderId) }
    val state by viewModel.state.collectAsState()
    var menuExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Order") },
                navigationIcon = { BackButton(rootNav) },
                actions = {
                    Box {
                        IconButton(onClick = { menuExpanded = true }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit")
                        }
                        DropdownMenu(
                            expanded = menuExpanded,
                            onDismissRequest = { menuExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Edit") },
                                onClick = { /* TODO: Add edit screen */ }
                            )
                            DropdownMenuItem(
                                text = { Text("Cancel") },
                                onClick = { menuExpanded = false }
                            )
                        }
                    }
                }
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

@Composable
private fun Loading() = Box(
    Modifier.fillMaxSize(), Alignment.Center
) { CircularProgressIndicator() }

@Composable
private fun ErrorText(text: String) = Box(
    Modifier.fillMaxSize(), Alignment.Center
) { Text("Error: $text", color = MaterialTheme.colorScheme.error) }

@Composable
private fun DetailsContent(order: OrderDto) = LazyColumn(
    modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp)
) {
    item {
        BigTextCard(order.title.orEmpty())
    }

    item {
        LabeledCard("Description", order.description.orEmpty())
    }

    item {
        ElevatedCard(Modifier.fillMaxWidth()) {
            Column(
                Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PriceRow(order.price)
                LocationInfo(address = order.location.name)
                OrderStatusChip(status = order.status)
                LabeledText("Created at", order.createAt.orEmpty())
            }
        }
    }

    item {
        LabeledCard("Services", order.serviceOfferings.joinToString("\n") { "- ${it.name}" })
    }
}

@Composable
private fun LabeledCard(label: String, content: String) {
    ElevatedCard(Modifier.fillMaxWidth()) {
        Column(
            Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(label, style = MaterialTheme.typography.titleMedium)
            Text(content, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
private fun LabeledText(label: String, value: String) = Row {
    Text("$label: ", style = MaterialTheme.typography.bodyMedium)
    Text(value, style = MaterialTheme.typography.bodyMedium)
}

@Preview(showBackground = true)
@Composable
private fun OrderDetailsPreview() {
    val fakeOrder = OrderDto(
        id = 1L,
        clientId = 2L,
        title = "Haircut at Home",
        description = "Need someone to come for a quick haircut.",
        price = 1000,
        location = LocationDto(1,"Prague"),
        status = OrderStatus.CREATED,
        createAt = "01.01.2000",
        serviceOfferings = listOf(ServiceOfferingDto(1, "Hair cut"))
    )

    MaterialTheme {
        Surface {
            DetailsContent(order = fakeOrder)
        }
    }
}

@Composable
private fun BigTextCard(text: String) {
    ElevatedCard(Modifier.fillMaxWidth()) {
        Box(Modifier.padding(16.dp)) {
            Text(
                text = text,
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}

@Composable
private fun PriceRow(price: Int) = Row(
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(6.dp)
) {
    Icon(
        imageVector = Icons.Default.MonetizationOn,
        contentDescription = "Price",
        tint = MaterialTheme.colorScheme.primary
    )
    Text(
        text = "$price",
        style = MaterialTheme.typography.bodyMedium
    )
}







