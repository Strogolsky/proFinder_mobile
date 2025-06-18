package cvut.fit.kot.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import cvut.fit.kot.ui.components.BackButton
import cvut.fit.kot.ui.viewModel.ChatViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    rootNav: NavHostController,
    viewModel: ChatViewModel = hiltViewModel(),
) {
    val state = viewModel.uiState
    var text by remember { mutableStateOf("") }
    val myId  by viewModel.myId.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    BackButton(navController = rootNav)
                },
                title = { Text("Chat") }
            )
        },

        bottomBar = {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = text,
                    onValueChange = { text = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Message") }
                )
                IconButton(
                    onClick = {
                        viewModel.send(text)
                        text = ""
                    }
                ) { Icon(Icons.Default.Send, null) }
            }
        }
    ) { padding ->
        when (state) {
            ChatViewModel.UiState.Loading -> Box(
                Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator() }

            is ChatViewModel.UiState.Error -> Box(
                Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) { Text(state.message) }

            is ChatViewModel.UiState.Success -> {
                val msgs = state.messages
                LazyColumn(
                    reverseLayout = true,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {
                    items(msgs.reversed(), key = { it.id }) { msg ->
                        MessageBubble(msg.content ?: "", msg.senderId == myId)
                    }
                }
            }
        }
    }
}
@Composable
private fun MessageBubble(text: String, isMe: Boolean) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start
    ) {
        Surface(
            color = if (isMe) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.surfaceVariant,
            shape = MaterialTheme.shapes.medium
        ) {
            Text(
                text = text,
                modifier = Modifier
                    .padding(10.dp)
                    .widthIn(max = 280.dp),
                color = if (isMe) Color.White else LocalContentColor.current,
                textAlign = TextAlign.Start
            )
        }
    }
}
