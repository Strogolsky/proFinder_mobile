package cvut.fit.kot.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import cvut.fit.kot.data.model.OrderStatus
import cvut.fit.kot.ui.viewModel.OrdersViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersScreen(
    rootNav: NavHostController,
    viewModel: OrdersViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text("Order") }) }
    ) { inner ->
        Box(Modifier.padding(inner)) {
            when (val s = state) {
                OrdersViewModel.State.Loading   -> LoadingBox()
                is OrdersViewModel.State.Error   -> ErrorBox(s.message)
                is OrdersViewModel.State.Success -> OrdersList(s.list)
            }
        }
    }
}

@Composable private fun LoadingBox() = Box(
    Modifier.fillMaxSize(),
    contentAlignment = Alignment.Center
) { CircularProgressIndicator() }

@Composable private fun ErrorBox(msg: String) = Box(
    Modifier.fillMaxSize(),
    contentAlignment = Alignment.Center
) { Text("Error: $msg", color = MaterialTheme.colorScheme.error) }

@Composable
private fun OrdersList(items: List<OrdersViewModel.OrderUi>) =
    LazyColumn(
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) { items(items, key = { it.id }) { OrderCard(it) } }

@Composable
private fun OrderCard(order: OrdersViewModel.OrderUi) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .clickable {},
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(order.description, style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(6.dp))
            Text("Services: ${order.serviceOfferings}", style = MaterialTheme.typography.bodySmall)
            Spacer(Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .background(order.status.tint(), RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    order.status.displayName(),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White
                )
            }
        }
    }
}

private fun OrderStatus.displayName(): String =
    name.lowercase().replace('_', ' ').replaceFirstChar { it.titlecase() }

@Composable
private fun OrderStatus.tint(): Color = when (this) {
    OrderStatus.CREATED        -> MaterialTheme.colorScheme.primary
    OrderStatus.CANCELLED      -> MaterialTheme.colorScheme.error
    OrderStatus.CLIENT_PENDING -> MaterialTheme.colorScheme.tertiary
    OrderStatus.COMPLETED      -> MaterialTheme.colorScheme.primary
    OrderStatus.REVIEWED       -> MaterialTheme.colorScheme.secondary
}

