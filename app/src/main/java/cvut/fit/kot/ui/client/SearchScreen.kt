package cvut.fit.kot.ui.client

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import cvut.fit.kot.data.model.SpecialistSearchResponse

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    rootNav: NavHostController,
    vm: SearchViewModel = hiltViewModel()
) {
    val state by vm.state.collectAsState()

    var showSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Search") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            SearchBar(
                query = state.query,
                onQueryChange = vm::onQueryChange,
                onSearch = vm::search
            )

            FilterChip(
                selected = false,
                onClick = { showSheet = true },
                label = {
                    Text("City: ${state.location.ifBlank { "All" }}")
                },
                modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
            )

            when {
                state.isLoading      -> LoadingSection()
                state.error != null  -> ErrorSection(state.error!!, vm::search)
                state.data.isEmpty() -> EmptySection()
                else                 -> ResultList(state.data) { specialist ->
                    rootNav.navigate("specialist/${specialist.id}")
                }
            }
        }
    }

    if (showSheet) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = { showSheet = false }
        ) {
            state.allLocations.forEach { loc ->
                ListItem(
                    headlineContent = { Text(loc.name) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            vm.selectLocation(loc.name)
                            showSheet = false
                        }
                        .padding(vertical = 8.dp, horizontal = 16.dp)
                )
            }
        }
    }
}

@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit
) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        singleLine = true,
        placeholder = { Text("Search for a specialist…") },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { onSearch() })
    )
}

@Composable
private fun LoadingSection() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorSection(message: String, onRetry: () -> Unit) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(message, color = MaterialTheme.colorScheme.error)
            Spacer(Modifier.height(8.dp))
            Button(onClick = onRetry) { Text("Retry") }
        }
    }
}

@Composable
private fun EmptySection() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("No results in your city")
    }
}

@Composable
private fun ResultList(
    items: List<SpecialistSearchResponse>,
    onClick: (SpecialistSearchResponse) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(items) { specialist ->
            SpecialistCard(specialist, onClick)
        }
    }
}

@Composable
private fun SpecialistCard(
    item: SpecialistSearchResponse,
    onClick: (SpecialistSearchResponse) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .fillMaxWidth()
            .clickable { onClick(item) },
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text("${item.firstName} ${item.lastName}",
                style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(4.dp))
            Text(item.description.orEmpty(),
                maxLines = 2,
                style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(4.dp))
            Text("⭐ ${item.averageRating}",
                style = MaterialTheme.typography.labelMedium)
        }
    }
}

