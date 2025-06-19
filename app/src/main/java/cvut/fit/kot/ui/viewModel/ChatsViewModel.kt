package cvut.fit.kot.ui.viewModel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import cvut.fit.kot.data.model.ChatResponse
import cvut.fit.kot.domain.useCase.GetChatsProfileUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatsViewModel @Inject constructor(
    private val getChatsProfileUseCase: GetChatsProfileUseCase
) : ViewModel() {

    sealed interface UiState {
        object Loading : UiState
        data class Success(val chats: List<ChatResponse>) : UiState
        data class Error(val message: String) : UiState
    }

    var uiState by mutableStateOf<UiState>(UiState.Loading)
        private set

    init { loadChats() }

    fun loadChats() = viewModelScope.launch {
        uiState = UiState.Loading
        val result = getChatsProfileUseCase.invoke()
        uiState = result.fold(
            onSuccess = { UiState.Success(it) },
            onFailure = { UiState.Error(it.localizedMessage ?: "Unknown error") }
        )
    }
}
