package cvut.fit.kot.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cvut.fit.kot.data.useCase.SignInUseCase
import cvut.fit.kot.data.useCase.SignUpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase,
    private val signInUseCase: SignInUseCase
) : ViewModel() {

    sealed interface UiState {
        object Idle : UiState
        object Loading : UiState
        object Success : UiState
        data class Error(val message: String) : UiState
    }

    private val _state = MutableStateFlow<UiState>(UiState.Idle)
    val state = _state.asStateFlow()

    fun signUp(email: String, password: String, role: String) =
        launchRequest { signUpUseCase.execute(email, password, role) }

    fun signIn(email: String, password: String, role: String) =
        launchRequest { signInUseCase.execute(email, password, role) }

    private inline fun launchRequest(
        crossinline block: suspend () -> Result<Unit>
    ) = viewModelScope.launch {
        _state.value = UiState.Loading
        _state.value = block()
            .fold(onSuccess = { UiState.Success },
                onFailure = { UiState.Error(it.message ?: "Unknown error") })
    }
}

