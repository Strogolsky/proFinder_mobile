package cvut.fit.kot.ui.client

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import cvut.fit.kot.data.model.ChatMessageResponse
import cvut.fit.kot.data.useCase.GetChatHistoryUseCase
import cvut.fit.kot.data.useCase.ObserveChatMessagesUseCase
import cvut.fit.kot.data.repository.SendMessageUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getChatHistory: GetChatHistoryUseCase,
    private val observeMessages: ObserveChatMessagesUseCase,
    private val sendMessage: SendMessageUseCase
) : ViewModel() {

    private val chatId: Long =
        checkNotNull(savedStateHandle["chatId"]) { "chatId argument is missing" }

    sealed interface UiState {
        object Loading : UiState
        data class Success(val messages: List<ChatMessageResponse>) : UiState
        data class Error(val message: String) : UiState
    }

    var uiState by mutableStateOf<UiState>(UiState.Loading)
        private set

    init {
        loadHistory()       // REST
        subscribeWs()       // WebSocket
    }

    private fun loadHistory() = viewModelScope.launch {
        uiState = UiState.Loading
        uiState = getChatHistory.execute(chatId).fold(
            { UiState.Success(it.sortedBy { m -> m.id }) },
            { UiState.Error(it.message ?: "Error") }
        )
    }

    private fun subscribeWs() = viewModelScope.launch {

        observeMessages(chatId).collect { incoming ->
            uiState = when (uiState) {
                is UiState.Success -> {
                    val current = (uiState as UiState.Success).messages
                    UiState.Success(current + incoming)
                }
                else -> uiState
            }
        }
    }

    fun send(text: String) = viewModelScope.launch {
        if (text.isNotBlank()) sendMessage(chatId, text.trim())
    }
}


