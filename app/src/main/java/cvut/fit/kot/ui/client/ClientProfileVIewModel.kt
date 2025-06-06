package cvut.fit.kot.ui.client

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cvut.fit.kot.data.model.ClientResponse
import cvut.fit.kot.data.useCase.GetAvatarUseCase
import cvut.fit.kot.data.useCase.GetClientProfileUseCase
import cvut.fit.kot.data.useCase.LogOutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClientProfileViewModel @Inject constructor(
    private val getProfile: GetClientProfileUseCase,
    private val getAvatarUseCase: GetAvatarUseCase,
    private val logOutUseCase: LogOutUseCase
) : ViewModel() {

    /* ---- UiState ---- */
    sealed interface UiState {
        object Loading : UiState
        data class Success(val user: ClientUiModel) : UiState
        data class Error(val throwable: Throwable) : UiState
    }

    private val _state = MutableStateFlow<UiState>(UiState.Loading)
    val state: StateFlow<UiState> = _state.asStateFlow()

    init { load() }

    /* ---- public API ---- */
    fun load() = viewModelScope.launch {
        _state.value = UiState.Loading
        try {
            val profile = getProfile.execute().getOrThrow()

            val avatarBytes = getAvatarUseCase(profile.id)
            val avatarBitmap = avatarBytes
                ?.let { BitmapFactory.decodeByteArray(it, 0, it.size) }
                ?.asImageBitmap()

            _state.value = UiState.Success(profile.toUi(avatarBitmap))
        } catch (t: Throwable) {
            _state.value = UiState.Error(t)
        }
    }
    fun logout(onDone: () -> Unit) = viewModelScope.launch {
        logOutUseCase.execute()
        onDone()
    }
}


/** UI‑friendly representation (flat & nullable‑free) */
data class ClientUiModel(
    val avatar: ImageBitmap?,
    val name: String,
    val city: String,
    val role: String,
    val email: String,
    val phone: String
)

/* --- mapping extension --- */
private fun ClientResponse.toUi(avatar: ImageBitmap?) = ClientUiModel(
    avatar = avatar,
    name   = "$firstName $lastName",
    city   = location?.name.orEmpty(),
    role   = "CLIENT",
    email  = email,
    phone  = phoneNumber.orEmpty()
)
