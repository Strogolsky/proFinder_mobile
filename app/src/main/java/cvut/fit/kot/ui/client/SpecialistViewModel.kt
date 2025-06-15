package cvut.fit.kot.ui.viewmodel

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cvut.fit.kot.data.model.CreateChatRequest
import cvut.fit.kot.data.model.SpecialistResponse
import cvut.fit.kot.data.useCase.CreateChatUseCase
import cvut.fit.kot.data.useCase.GetAvatarUseCase
import cvut.fit.kot.data.useCase.GetSpecialistByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class SpecialistViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getDetail: GetSpecialistByIdUseCase,
    private val getAvatar: GetAvatarUseCase,
    private val createChat: CreateChatUseCase
) : ViewModel() {


    /* ---------- UI-state ---------- */
    sealed interface UiState {
        object Loading : UiState
        data class Success(val data: SpecialistUiModel) : UiState
        data class Error(val throwable: Throwable) : UiState
    }

    private val _state = MutableStateFlow<UiState>(UiState.Loading)
    val state: StateFlow<UiState> = _state

    private val id: Long = checkNotNull(savedStateHandle["id"])

    init { load() }

    private fun load() = viewModelScope.launch {
        _state.value = UiState.Loading
        try {
            val detail = getDetail(id).getOrThrow()

            val avatarBytes = getAvatar(id)
            val avatarBitmap = avatarBytes
                ?.let { BitmapFactory.decodeByteArray(it, 0, it.size) }
                ?.asImageBitmap()

            _state.value = UiState.Success(detail.toUi(avatarBitmap))
        } catch (t: Throwable) {
            _state.value = UiState.Error(t)
        }
    }

    fun openChat(onReady: (Long) -> Unit) = viewModelScope.launch {
        try {
            val resp = createChat(CreateChatRequest(recipientId = id))

            if (resp.isSuccessful) {
                val chatId = resp.body()?.chatId
                    ?: throw IllegalStateException("Empty body")

                onReady(chatId)
            } else {
                throw HttpException(resp)
            }

        } catch (e: Exception) {
            _state.value = UiState.Error(e)
        }
    }

}

/* --- UI model & mapper --- */
data class SpecialistUiModel(
    val avatar: ImageBitmap?,
    val name: String,
    val city: String,
    val rating: Double,
    val services: List<String>,
    val description: String,
    val email: String,
    val phone: String
)

private fun SpecialistResponse.toUi(img: ImageBitmap?) = SpecialistUiModel(
    avatar       = img,
    name         = "$firstName $lastName",
    city         = location.name,
    rating       = averageRating,
    services     = serviceOfferings.map { it.name },
    description  = description,
    email        = email,
    phone        = phoneNumber
)
