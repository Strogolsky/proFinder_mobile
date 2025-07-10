package cvut.fit.kot.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import cvut.fit.kot.data.model.OrderStatus
import cvut.fit.kot.ui.components.OrderStatusChip
import cvut.fit.kot.ui.viewModel.OrdersViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderListScreen(
    rootNav: NavHostController,
    viewModel: OrdersViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Orders") },
                actions = {
                    IconButton(onClick = { /* TODO: handle create order click */ }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Create Order"
                        )
                    }
                }
            )
        }
    ) { inner ->
        Box(Modifier.padding(inner)) {
            when (val s = state) {
                OrdersViewModel.State.Loading -> LoadingBox()
                is OrdersViewModel.State.Error -> ErrorBox(s.message)
                is OrdersViewModel.State.Success -> OrdersList(s.list, rootNav)
            }
        }
    }
}

@Composable
private fun LoadingBox() = Box(
    Modifier.fillMaxSize(),
    Alignment.Center
) {
    CircularProgressIndicator()
}

@Composable
private fun ErrorBox(msg: String) = Box(
    Modifier.fillMaxSize(),
    Alignment.Center
) {
    Text("Error: $msg", color = MaterialTheme.colorScheme.error)
}

@Composable
private fun OrdersList(
    items: List<OrdersViewModel.OrderUi>,
    rootNav: NavHostController
) = LazyColumn(
    contentPadding = PaddingValues(8.dp),
    verticalArrangement = Arrangement.spacedBy(8.dp)
) {
    items(items, key = { it.id }) { ui ->
        OrderCard(ui) { rootNav.navigate("order/${ui.id}") }
    }
}

@Composable
private fun OrderCard(
    order: OrdersViewModel.OrderUi,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier.padding(vertical = 16.dp, horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = order.title,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(12.dp))
            OrderStatusChip(status = order.status)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun OrderCardPreview() {
    MaterialTheme {
        OrderCard(
            order = OrdersViewModel.OrderUi(
                id = 1L,
                title = "Text",
                status = OrderStatus.CREATED
            ),
            onClick = {}
        )
    }
}




