package cvut.fit.kot.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cvut.fit.kot.data.model.OrderStatus

@Composable
fun OrderStatusChip(
    status: OrderStatus,
    modifier: Modifier = Modifier
) {
    val label = status.name
        .lowercase()
        .replace('_', ' ')
        .replaceFirstChar { it.titlecase() }

    val bg = when (status) {
        OrderStatus.CREATED        -> MaterialTheme.colorScheme.primary
        OrderStatus.CANCELLED      -> MaterialTheme.colorScheme.error
        OrderStatus.CLIENT_PENDING -> MaterialTheme.colorScheme.tertiary
        OrderStatus.COMPLETED      -> MaterialTheme.colorScheme.primary
        OrderStatus.REVIEWED       -> MaterialTheme.colorScheme.secondary
    }

    AssistChip(
        onClick = { },
        label   = { Text(label, color = Color.White) },
        modifier = modifier.padding(0.dp),
        colors  = AssistChipDefaults.assistChipColors(containerColor = bg)
    )
}
