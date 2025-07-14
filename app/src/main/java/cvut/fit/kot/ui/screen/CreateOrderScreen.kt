package cvut.fit.kot.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import cvut.fit.kot.data.model.LocationDto
import cvut.fit.kot.data.model.OrderRequest
import cvut.fit.kot.data.model.ServiceOfferingDto
import cvut.fit.kot.ui.components.BackButton
import cvut.fit.kot.ui.components.CenterError
import cvut.fit.kot.ui.components.CenterProgress
import cvut.fit.kot.ui.components.LocationPicker
import cvut.fit.kot.ui.components.NumberInputField
import cvut.fit.kot.ui.components.ServiceOfferingPickerSheet
import cvut.fit.kot.ui.components.TextInputField
import cvut.fit.kot.ui.viewModel.CreateOrderViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateOrderScreen(
    rootNav: NavHostController,
    vm: CreateOrderViewModel = hiltViewModel()
) {
    val st by vm.state.collectAsState()
    LaunchedEffect(Unit) { vm.load() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("New Order") },
                navigationIcon = { BackButton(navController = rootNav) },
                actions = {
                    TextButton(
                        enabled = st.form != null && !st.saving,
                        onClick = { vm.save { rootNav.popBackStack() } }
                    ) { Text("Create") }
                }
            )
        }
    ) { pad ->
        when {
            st.loading -> CenterProgress(pad)
            st.error != null -> CenterError(pad, st.error!!)
            st.form != null -> OrderForm(
                modifier = Modifier
                    .padding(pad)
                    .padding(24.dp),
                f   = st.form!!,
                loc = st.locations,
                availableServices = st.allServiceOfferings,
                onTitle = vm::setTitle,
                onDesc  = vm::setDesc,
                onPrice = { vm.setPrice(it.toIntOrNull() ?: 0) },
                onLoc   = vm::setLoc,
                onSelectServices = vm::setServiceOfferings
            )
        }
    }
}

@Composable
private fun OrderForm(
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
        TextInputField(value = f.title, onValueChange = onTitle, label = "Title")
        TextInputField(value = f.description, onValueChange = onDesc, label = "Description")
        NumberInputField(value = f.price.toString(), onValueChange = onPrice, label = "Price")
        LocationPicker(all = loc, selected = f.location, onSelect = onLoc)
        ServiceOfferingPickerSheet(
            all       = availableServices,
            selected  = f.serviceOfferings,
            onConfirm = onSelectServices
        )
    }
}
