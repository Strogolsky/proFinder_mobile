package cvut.fit.kot.ui.components


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cvut.fit.kot.data.model.ServiceOfferingDto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceOfferingPickerSheet(
    all: List<ServiceOfferingDto>,
    selected: List<ServiceOfferingDto>,
    onConfirm: (List<ServiceOfferingDto>) -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showSheet by remember { mutableStateOf(false) }

    OutlinedButton(
        onClick = { showSheet = true },
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            if (selected.isEmpty()) "Select services"
            else selected.joinToString { it.name }
        )
    }

    // —–––– Сам bottom-sheet
    if (showSheet) {
        var tempSelected by remember { mutableStateOf(selected) }

        ModalBottomSheet(
            onDismissRequest = { showSheet = false },
            sheetState = sheetState
        ) {
            Text(
                "Select services",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            LazyColumn(Modifier.fillMaxHeight(0.7f)) {
                items(all, key = { it.id }) { svc ->
                    val checked = svc in tempSelected
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                tempSelected =
                                    if (checked) tempSelected - svc else tempSelected + svc
                            }
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Checkbox(checked = checked, onCheckedChange = null)
                        Spacer(Modifier.width(12.dp))
                        Text(svc.name)
                    }
                }
            }

            Button(
                onClick = {
                    onConfirm(tempSelected)
                    showSheet = false
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) { Text("OK") }
        }
    }
}







