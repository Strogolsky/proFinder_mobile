package cvut.fit.kot.domain.useCase

import cvut.fit.kot.data.model.OrderDto
import cvut.fit.kot.data.repository.OrderRepository
import javax.inject.Inject

class GetOrderByOrderIdUseCase @Inject constructor(
    private val repository: OrderRepository
) {
    suspend operator fun invoke(orderId: Long): Result<OrderDto> {
        return repository.getByOrderId(orderId);
    }
}