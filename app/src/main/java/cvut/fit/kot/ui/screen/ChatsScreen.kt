package cvut.fit.kot.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import cvut.fit.kot.ui.viewModel.ChatsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatsScreen(
    rootNav: NavHostController,
    vm: ChatsViewModel = hiltViewModel()
) {
    val state = vm.uiState

    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text("Chats") }) }
    ) { padding ->
        when (state) {
            ChatsViewModel.UiState.Loading -> Box(
                Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator() }

            is ChatsViewModel.UiState.Error -> Box(
                Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) { Text(text = state.message ?: "") }

            is ChatsViewModel.UiState.Success -> {
                val chats = state.chats
                LazyColumn(
                    contentPadding = PaddingValues(
                        top = padding.calculateTopPadding(),
                        bottom = 16.dp
                    )
                ) {
                    items(chats, key = { it.chatId }) { chat ->
                        ChatItem(
                            partnerName = chat.partnerFirstName,
                            onClick = { rootNav.navigate("chat/${chat.chatId}") }
                        )
                        Divider()
                    }
                }
            }
        }
    }
}

@Composable
private fun ChatItem(
    partnerName: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val displayName = partnerName?.takeIf { it.isNotBlank() } ?: "No name"

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 24.dp, vertical = 30.dp)
    ) {
        Spacer(Modifier.width(16.dp))
        Text(
            text = displayName,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

