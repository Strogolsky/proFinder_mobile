package cvut.fit.kot.domain.useCase.order

import cvut.fit.kot.data.model.OrderRequest
import cvut.fit.kot.data.model.OrderResponse
import cvut.fit.kot.data.repository.OrderRepository
import javax.inject.Inject

class UpdateOrderUseCase @Inject constructor(
    private val repository: OrderRepository
) {
    suspend operator fun invoke(orderId: Long, req: OrderRequest): Result<OrderResponse> {
        return repository.update(orderId, req)
    }
}