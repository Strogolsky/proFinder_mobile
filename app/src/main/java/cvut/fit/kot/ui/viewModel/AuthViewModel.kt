package cvut.fit.kot.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cvut.fit.kot.data.repository.SessionRepository
import cvut.fit.kot.domain.useCase.SignInUseCase
import cvut.fit.kot.domain.useCase.SignUpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase,
    private val signInUseCase: SignInUseCase,
    private val sessionRepository: SessionRepository
) : ViewModel() {

    sealed interface UiState {
        object Idle    : UiState
        object Loading : UiState
        object Success : UiState
        data class Error(val message: String) : UiState
    }

    private val _state = MutableStateFlow<UiState>(UiState.Idle)
    val state = _state.asStateFlow()

    val tokenFlow: Flow<String?> = sessionRepository.tokenFlow()
    val roleFlow : Flow<String?> = sessionRepository.roleFlow()

    fun signUp(email: String, password: String, role: String) = launchRequest {
        signUpUseCase.invoke(email, password, role)
    }

    fun signIn(email: String, password: String, role: String) = launchRequest {
        signInUseCase.invoke(email, password, role)
    }

    private inline fun launchRequest(
        crossinline block: suspend () -> Result<Unit>
    ) = viewModelScope.launch {
        _state.value = UiState.Loading
        _state.value = block()
            .fold(
                onSuccess = { UiState.Success },
                onFailure = { UiState.Error(it.message ?: "Unknown error") }
            )
    }

    fun clearError() {
        if (_state.value is UiState.Error) _state.value = UiState.Idle
    }

    fun resetState() {
        _state.value = UiState.Idle
    }
}
