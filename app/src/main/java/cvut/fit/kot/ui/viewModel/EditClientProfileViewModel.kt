package cvut.fit.kot.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cvut.fit.kot.data.model.ClientRequest
import cvut.fit.kot.data.model.ClientResponse
import cvut.fit.kot.data.model.LocationDto
import cvut.fit.kot.domain.useCase.GetAllLocationsUseCase
import cvut.fit.kot.domain.useCase.GetClientProfileUseCase
import cvut.fit.kot.domain.useCase.UpdateClientProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val getProfile: GetClientProfileUseCase,
    private val updateProfile: UpdateClientProfileUseCase,
    private val getLocations: GetAllLocationsUseCase
) : ViewModel() {

    data class UiState(
        val loading: Boolean = true,
        val saving : Boolean = false,
        val error  : String? = null,
        val form   : ClientRequest? = null,
        val locations: List<LocationDto> = emptyList()
    )

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state

    fun load() = viewModelScope.launch {
        _state.value = UiState(loading = true)
        runCatching {
            val profile   = async { getProfile.invoke().getOrThrow() }
            val locations = async { getLocations.invoke().getOrThrow() }

            _state.value.copy(
                loading   = false,
                form      = profile.await().toRequest(),
                locations = locations.await()
            )
        }.onSuccess { newState ->
            _state.value = newState
        }.onFailure { t ->
            _state.value = UiState(loading = false, error = t.localizedMessage)
        }
    }

    fun setFirst (v: String)       = edit { copy(firstName = v) }
    fun setLast  (v: String)       = edit { copy(lastName  = v) }
    fun setPhone (v: String)       = edit { copy(phoneNumber = v) }
    fun setLoc   (l: LocationDto?) = edit { copy(location   = l) }

    private inline fun edit(block: ClientRequest.() -> ClientRequest) =
        _state.update { st -> st.form?.let { st.copy(form = it.block()) } ?: st }

    fun save(onOk: () -> Unit) = viewModelScope.launch {
        val req = _state.value.form ?: return@launch
        _state.update { it.copy(saving = true, error = null) }

        runCatching { updateProfile.invoke(req) }
            .onSuccess { updated ->
                _state.value = _state.value.copy(
                    saving = false,
                    form   = updated.toRequest()
                )
                onOk()
            }
            .onFailure { t ->
                _state.update { it.copy(saving = false, error = t.localizedMessage) }
            }
    }
}

private fun ClientResponse.toRequest() = ClientRequest(
    firstName   = firstName.orEmpty(),
    lastName    = lastName.orEmpty(),
    phoneNumber = phoneNumber.orEmpty(),
    location    = location
)


