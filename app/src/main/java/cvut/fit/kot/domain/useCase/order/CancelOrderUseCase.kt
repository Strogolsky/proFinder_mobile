package cvut.fit.kot.domain.useCase.order

import cvut.fit.kot.data.model.OrderResponse
import cvut.fit.kot.data.repository.OrderRepository
import javax.inject.Inject

class CancelOrderUseCase @Inject constructor(
    private val repository: OrderRepository
) {
    suspend operator fun invoke(orderId: Long): Result<OrderResponse> {
        return repository.cancel(orderId);
    }
}