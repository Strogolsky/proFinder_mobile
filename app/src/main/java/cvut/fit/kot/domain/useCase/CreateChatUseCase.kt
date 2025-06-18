package cvut.fit.kot.domain.useCase

import cvut.fit.kot.data.model.ChatResponse
import cvut.fit.kot.data.model.CreateChatRequest
import cvut.fit.kot.data.repository.ChatRepository
import retrofit2.Response
import javax.inject.Inject

class CreateChatUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(request: CreateChatRequest): Response<ChatResponse>
        = repository.create(request)
}