package cvut.fit.kot.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cvut.fit.kot.data.model.OrderResponse
import cvut.fit.kot.domain.useCase.order.CancelOrderUseCase
import cvut.fit.kot.domain.useCase.order.GetOrderByOrderIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderDetailsViewModel @Inject constructor(
    private val getOrder: GetOrderByOrderIdUseCase,
    private val cancelOrder: CancelOrderUseCase
) : ViewModel() {

    sealed interface State {
        object Loading : State
        data class Success(val order: OrderResponse) : State
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

    fun cancel(orderId: Long) = viewModelScope.launch {
        _state.value = State.Loading
        cancelOrder(orderId).fold(
            onSuccess = { updatedOrder -> _state.value = State.Success(updatedOrder) },
            onFailure = { _state.value = State.Error(it.message ?: "Failed to cancel") }
        )
    }
}

