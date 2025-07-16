package cvut.fit.kot.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import cvut.fit.kot.ui.components.*
import cvut.fit.kot.ui.viewModel.EditOrderViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditOrderScreen(
    orderId: Long,
    rootNav: NavHostController,
    vm: EditOrderViewModel = hiltViewModel()
) {
    LaunchedEffect(orderId) { vm.load(orderId) }

    val st by vm.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Order") },
                navigationIcon = { BackButton(rootNav) },
                actions = {
                    TextButton(
                        enabled = st.form != null && !st.saving,
                        onClick = { vm.save(orderId) { rootNav.popBackStack() } }
                    ) { Text("Save") }
                }
            )
        }
    ) { pad ->
        when {
            st.loading       -> CenterProgress(pad)
            st.error != null -> CenterError(pad, st.error!!)
            st.form != null  -> OrderForm(
                modifier          = Modifier.padding(pad).padding(24.dp),
                f                 = st.form!!,
                loc               = st.locations,
                availableServices = st.allServiceOfferings,
                onTitle           = vm::setTitle,
                onDesc            = vm::setDesc,
                onPrice           = { vm.setPrice(it.toIntOrNull() ?: 0) },
                onLoc             = vm::setLoc,
                onSelectServices  = vm::setServiceOfferings
            )
        }
    }
}


