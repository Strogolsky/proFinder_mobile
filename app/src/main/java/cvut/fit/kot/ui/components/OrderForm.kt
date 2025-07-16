package cvut.fit.kot.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cvut.fit.kot.data.model.LocationDto
import cvut.fit.kot.data.model.OrderRequest
import cvut.fit.kot.data.model.ServiceOfferingDto

@Composable
fun OrderForm(
    modifier: Modifier = Modifier,
    f: OrderRequest,
    loc: List<LocationDto>,
    availableServices: List<ServiceOfferingDto>,
    onTitle: (String) -> Unit,
    onDesc : (String) -> Unit,
    onPrice: (String) -> Unit,
    onLoc  : (LocationDto) -> Unit,
    onSelectServices: (List<ServiceOfferingDto>) -> Unit
) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        TextInputField(
            value         = f.title,
            onValueChange = onTitle,
            label         = "Title"
        )
        TextInputField(
            value         = f.description,
            onValueChange = onDesc,
            label         = "Description"
        )
        NumberInputField(
            value         = f.price.toString(),
            onValueChange = onPrice,
            label         = "Price"
        )
        LocationPicker(
            all      = loc,
            selected = f.location,
            onSelect = onLoc
        )
        ServiceOfferingPickerSheet(
            all       = availableServices,
            selected  = f.serviceOfferings,
            onConfirm = onSelectServices
        )
    }
}