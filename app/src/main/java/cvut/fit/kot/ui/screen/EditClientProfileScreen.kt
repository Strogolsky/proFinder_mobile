package cvut.fit.kot.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import cvut.fit.kot.data.model.ClientRequest
import cvut.fit.kot.data.model.LocationDto
import cvut.fit.kot.ui.components.BackButton
import cvut.fit.kot.ui.components.NumberInputField
import cvut.fit.kot.ui.components.TextInputField
import cvut.fit.kot.ui.viewModel.EditProfileViewModel
import cvut.fit.kot.ui.components.CenterProgress

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    rootNav: NavHostController,
    vm : EditProfileViewModel = hiltViewModel()
) {
    val st by vm.state.collectAsState()
    LaunchedEffect(Unit) { vm.load() }

    Scaffold(
        topBar = { TopAppBar(
            title = { Text("Edit profile") },
            navigationIcon = {
                BackButton(navController = rootNav)
            },
            actions = {
                TextButton(
                    enabled = st.form != null && !st.saving,
                    onClick = {
                        vm.save {
                            rootNav.previousBackStackEntry
                                ?.savedStateHandle
                                ?.set("profile_updated", true)

                            rootNav.popBackStack()
                        }
                    }
                ) { Text("Save") }
            }
        )}
    ) { pad ->
        when {
            st.loading -> CenterProgress(pad)
            st.error != null -> CenterError(pad, st.error!!)
            st.form != null -> EditForm(
                Modifier.padding(pad).padding(24.dp),
                f   = st.form!!,
                loc = st.locations,
                onFirst = vm::setFirst,
                onLast  = vm::setLast,
                onPhone = vm::setPhone,
                onLoc   = vm::setLoc
            )
        }
    }
}

@Composable
private fun EditForm(
    modifier: Modifier = Modifier,
    f: ClientRequest,
    loc: List<LocationDto>,
    onFirst: (String) -> Unit,
    onLast : (String) -> Unit,
    onPhone: (String) -> Unit,
    onLoc  : (LocationDto) -> Unit
) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {

        TextInputField(
            value = f.firstName.orEmpty(),
            onValueChange = onFirst,
            label = "First name"
        )

        TextInputField(
            value = f.lastName.orEmpty(),
            onValueChange = onLast,
            label = "Last name"
        )

        NumberInputField(
            value = f.phoneNumber.orEmpty(),
            onValueChange = onPhone,
            label = "Phone"
        )

        LocationPicker(
            all       = loc,
            selected  = f.location,
            onSelect  = onLoc,
            modifier  = Modifier.fillMaxWidth()
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LocationPicker(
    all: List<LocationDto>,
    selected: LocationDto?,
    onSelect: (LocationDto) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
            .fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selected?.name.orEmpty(),
            onValueChange = {},
            readOnly = true,
            label = { Text("City") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            all.forEach { loc ->
                DropdownMenuItem(
                    text = { Text(loc.name) },
                    onClick = {
                        expanded = false
                        onSelect(loc)
                    }
                )
            }
        }
    }
}


@Composable private fun CenterError(pad: PaddingValues, msg: String) =
    Box(Modifier.fillMaxSize().padding(pad), Alignment.Center) { Text("Error: $msg") }