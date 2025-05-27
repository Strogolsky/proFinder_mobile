package cvut.fit.kot.ui.client

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cvut.fit.kot.data.model.ClientResponse
import cvut.fit.kot.data.useCase.GetClientProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClientProfileViewModel @Inject constructor(
    private val getProfile: GetClientProfileUseCase
) : ViewModel() {

    sealed interface UiState {
        object Loading : UiState
        data class Success(val user: ClientUiModel) : UiState
        data class Error(val throwable: Throwable) : UiState
    }

    private val _state = MutableStateFlow<UiState>(UiState.Loading)
    val state: StateFlow<UiState> = _state.asStateFlow()

    init { load() }

    fun load() {
        viewModelScope.launch {
            _state.value = UiState.Loading
            _state.value = getProfile.execute().fold(
                onSuccess = { UiState.Success(it.toUi()) },
                onFailure = { UiState.Error(it) }
            )
        }
    }
}

/** UI‑friendly representation (flat & nullable‑free) */
data class ClientUiModel(
    val avatar: String,
    val name: String,
    val city: String,
    val role: String,
    val email: String,
    val phone: String
)

/* --- mapping extension --- */
private fun ClientResponse.toUi() = ClientUiModel(
    avatar = avatarUrl ?: "https://i.pravatar.cc/256",
    name   = "$firstName $lastName",
    city   = location?.name ?: "",
    role   = "CLIENT",
    email  = email,
    phone = phoneNumber ?: ""
)
