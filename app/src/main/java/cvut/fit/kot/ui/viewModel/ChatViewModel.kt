package cvut.fit.kot.ui.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import cvut.fit.kot.data.model.ChatMessageResponse
import cvut.fit.kot.domain.useCase.GetChatHistoryUseCase
import cvut.fit.kot.domain.useCase.ObserveChatMessagesUseCase
import cvut.fit.kot.domain.useCase.SendMessageUseCase
import cvut.fit.kot.data.repository.SessionRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getChatHistory: GetChatHistoryUseCase,
    private val observeMessages: ObserveChatMessagesUseCase,
    private val sendMessage: SendMessageUseCase,
    private val sessionRepository: SessionRepository
) : ViewModel() {

    private val chatId: Long =
        checkNotNull(savedStateHandle["chatId"]) { "chatId argument is missing" }

    val myId = sessionRepository.userIdFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

    sealed interface UiState {
        object Loading : UiState
        data class Success(val messages: List<ChatMessageResponse>) : UiState
        data class Error(val message: String) : UiState
    }

    var uiState by mutableStateOf<UiState>(UiState.Loading)
        private set


    init {
        loadHistory()
        subscribeWs()
    }

    private fun loadHistory() = viewModelScope.launch {
        uiState = UiState.Loading
        uiState = getChatHistory(chatId).fold(
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


