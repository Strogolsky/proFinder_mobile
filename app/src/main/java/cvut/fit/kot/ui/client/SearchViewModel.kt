package cvut.fit.kot.ui.client

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cvut.fit.kot.data.model.LocationDto
import cvut.fit.kot.data.model.SpecialistSearchRequest
import cvut.fit.kot.data.model.SpecialistSearchResponse
import cvut.fit.kot.data.useCase.GetAllLocationsUseCase
import cvut.fit.kot.data.useCase.SpecialistSearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchUseCase: SpecialistSearchUseCase,
    private val locationsUseCase: GetAllLocationsUseCase
) : ViewModel() {

    data class UiState(
        val query: String = "",
        val isLoading: Boolean = false,
        val error: String? = null,
        val data: List<SpecialistSearchResponse> = emptyList(),
        val location: String = "Prague",
        val allLocations: List<LocationDto> = emptyList()
    )

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state

    private var searchJob: Job? = null
    private var debounceJob: Job? = null

    init {
        loadLocations()
    }

    fun onQueryChange(text: String) {
        _state.update { it.copy(query = text) }
        debounceSearch()
    }

    fun search() {
        performSearch()
    }

    fun selectLocation(location: String) {
        _state.update { it.copy(location = location) }
        performSearch()
    }

    private fun loadLocations() = viewModelScope.launch {
        locationsUseCase.execute()
            .onSuccess { list -> _state.update { it.copy(allLocations = list) } }
    }

    private fun debounceSearch() {
        debounceJob?.cancel()

        if (state.value.query.isBlank()) {
            _state.update { it.copy(data = emptyList(), error = null) }
            return
        }

        debounceJob = viewModelScope.launch {
            delay(400)
            performSearch()
        }
    }

    private fun performSearch() {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            val req = SpecialistSearchRequest(
                query = state.value.query,
                location = state.value.location

            )

            val result: Result<List<SpecialistSearchResponse>> =
                searchUseCase.execute(req)

            if (result.isSuccess) {
                val list = result.getOrNull().orEmpty()
                _state.update {
                    it.copy(
                        data = list,
                        isLoading = false,
                        error = null
                    )
                }
            } else {
                val message = result.exceptionOrNull()
                    ?.localizedMessage ?: "Unknown error"
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = message
                    )
                }
            }
        }
    }
}
