package cvut.fit.kot.data.model

data class SpecialistSearchResponse(
    val id: Long,
    val firstName: String?,
    val lastName: String?,
    val description: String?,
    val averageRating: Double,
    val services: List<String>,
    val location: LocationDto
)
