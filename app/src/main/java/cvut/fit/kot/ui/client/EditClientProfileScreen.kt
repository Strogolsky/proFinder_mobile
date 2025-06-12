package cvut.fit.kot.ui.client

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import cvut.fit.kot.data.model.ClientRequest
import cvut.fit.kot.data.model.LocationDto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    nav: NavHostController,
    vm : EditProfileViewModel = hiltViewModel()
) {
    val st by vm.state.collectAsState()
    LaunchedEffect(Unit) { vm.load() }

    Scaffold(
        topBar = { TopAppBar(
            title = { Text("Edit profile") },
            navigationIcon = { IconButton({ nav.popBackStack() }) {
                Icon(Icons.AutoMirrored.Outlined.ArrowBack, null) } },
            actions = {
                TextButton(
                    enabled = st.form != null && !st.saving,
                    onClick = {
                        vm.save {
                            nav.previousBackStackEntry
                                ?.savedStateHandle
                                ?.set("profile_updated", true)

                            nav.popBackStack()
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

        OutlinedTextField(
            value = f.firstName.orEmpty(),
            onValueChange = onFirst,
            label = { Text("First name") }
        )

        OutlinedTextField(
            value = f.lastName.orEmpty(),
            onValueChange = onLast,
            label = { Text("Last name") }
        )

        OutlinedTextField(
            value = f.phoneNumber.orEmpty(),
            onValueChange = onPhone,
            label = { Text("Phone") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
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
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selected?.name ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text("City") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = Modifier.menuAnchor()
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

@Composable private fun CenterProgress(pad: PaddingValues) =
    Box(Modifier.fillMaxSize().padding(pad), Alignment.Center) { CircularProgressIndicator() }

@Composable private fun CenterError(pad: PaddingValues, msg: String) =
    Box(Modifier.fillMaxSize().padding(pad), Alignment.Center) { Text("Error: $msg") }