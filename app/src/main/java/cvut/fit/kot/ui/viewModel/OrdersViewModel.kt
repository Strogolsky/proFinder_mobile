package cvut.fit.kot.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cvut.fit.kot.data.model.OrderResponse
import cvut.fit.kot.data.model.OrderStatus
import cvut.fit.kot.domain.useCase.order.GetOrdersByClientUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val getOrdersByClient: GetOrdersByClientUseCase
) : ViewModel() {

    /* ───────── local UI-model ───────── */
    data class OrderUi(
        val id: Long,
        val title: String,
        val status: OrderStatus,
    )

    private fun OrderResponse.toUi() = OrderUi(
        id = id,
        title = title,
        status = status,
    )
    /* ────────────────────────────────── */

    sealed interface State {
        object Loading : State
        data class Success(val list: List<OrderUi>) : State
        data class Error(val message: String) : State
    }

    private val _state = MutableStateFlow<State>(State.Loading)
    val state: StateFlow<State> = _state.asStateFlow()

    init { loadOrders() }

    fun loadOrders() = viewModelScope.launch {
        _state.value = State.Loading
        getOrdersByClient().fold(
            onSuccess = { list -> _state.value = State.Success(list.map { it.toUi() }) },
            onFailure = { err  -> _state.value = State.Error(err.message ?: "Unknown error") }
        )
    }
}