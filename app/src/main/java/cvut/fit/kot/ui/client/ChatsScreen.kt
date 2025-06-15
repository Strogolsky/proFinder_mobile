package cvut.fit.kot.ui.client

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatsScreen(
    rootNav: NavHostController,
    viewModel: ChatsViewModel = hiltViewModel()
) {
    val state = viewModel.uiState

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
            ) { Text(state.message) }

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
    partnerName: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 24.dp, vertical = 30.dp)
    ) {
        Spacer(Modifier.width(16.dp))
        Text(
            text = partnerName,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
