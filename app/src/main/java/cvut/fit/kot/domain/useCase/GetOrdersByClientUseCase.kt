package cvut.fit.kot.domain.useCase

import cvut.fit.kot.data.model.OrderDto
import cvut.fit.kot.data.repository.OrderRepository
import javax.inject.Inject

class GetOrdersByClientUseCase @Inject constructor(
    private val repository: OrderRepository
) {
    suspend operator fun invoke(): Result<List<OrderDto>> {
        return repository.getByClient();
    }
}