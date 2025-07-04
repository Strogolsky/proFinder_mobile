package cvut.fit.kot.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cvut.fit.kot.data.model.OrderDto
import cvut.fit.kot.domain.useCase.GetOrderByOrderIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderDetailsViewModel @Inject constructor(
    private val getOrder: GetOrderByOrderIdUseCase
) : ViewModel() {

    sealed interface State {
        object Loading : State
        data class Success(val order: OrderDto) : State
        data class Error(val msg: String) : State
    }

    private val _state = MutableStateFlow<State>(State.Loading)
    val state: StateFlow<State> = _state.asStateFlow()

    fun load(orderId: Long) = viewModelScope.launch {
        _state.value = State.Loading
        getOrder(orderId).fold(
            onSuccess = { _state.value = State.Success(it) },
            onFailure = { _state.value = State.Error(it.message ?: "Unknown error") }
        )
    }
}
