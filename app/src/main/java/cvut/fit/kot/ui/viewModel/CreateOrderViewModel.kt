package cvut.fit.kot.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cvut.fit.kot.data.model.LocationDto
import cvut.fit.kot.data.model.OrderRequest
import cvut.fit.kot.data.model.ServiceOfferingDto
import cvut.fit.kot.domain.useCase.GetAllServiceOfferingUseCase
import cvut.fit.kot.domain.useCase.location.GetAllLocationsUseCase
import cvut.fit.kot.domain.useCase.order.CreateOrderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateOrderViewModel @Inject constructor(
    private val createOrder: CreateOrderUseCase,
    private val getLocations: GetAllLocationsUseCase,
    private val getServiceOfferings: GetAllServiceOfferingUseCase
) : ViewModel() {

    data class UiState(
        val loading: Boolean = true,
        val saving: Boolean = false,
        val error: String? = null,
        val form: OrderRequest? = null,
        val locations: List<LocationDto> = emptyList(),
        val allServiceOfferings: List<ServiceOfferingDto> = emptyList()
    )

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    fun load() = viewModelScope.launch {
        _state.value = UiState(loading = true)

        runCatching {
            val locationsDeferred = getLocations()
            val servicesDeferred  = getServiceOfferings()

            val locations = locationsDeferred.getOrThrow()
            val services  = servicesDeferred.getOrThrow()

            UiState(
                loading = false,
                locations = locations,
                allServiceOfferings = services,
                form = OrderRequest("", "", 0, locations.first(), emptyList())
            )
        }.onSuccess {
            _state.value = it
        }.onFailure {
            _state.value = UiState(loading = false, error = it.message ?: "Unknown error")
        }
    }

    fun setTitle(v: String) = update { copy(title = v) }
    fun setDesc(v: String) = update { copy(description = v) }
    fun setPrice(v: Int) = update { copy(price = v) }
    fun setLoc(l: LocationDto) = update { copy(location = l) }
    fun setServiceOfferings(s: List<ServiceOfferingDto>) = update { copy(serviceOfferings = s) }

    private inline fun update(block: OrderRequest.() -> OrderRequest) =
        _state.update { it.form?.let { f -> it.copy(form = f.block()) } ?: it }

    fun save(onOk: () -> Unit) = viewModelScope.launch {
        val req = _state.value.form ?: return@launch
        _state.update { it.copy(saving = true, error = null) }

        createOrder(req)
            .onSuccess {
                _state.update { it.copy(saving = false) }
                onOk()
            }
            .onFailure { t ->
                _state.value = _state.value.copy(saving = false, error = t.message ?: "Unknown error")
            }
    }
}

