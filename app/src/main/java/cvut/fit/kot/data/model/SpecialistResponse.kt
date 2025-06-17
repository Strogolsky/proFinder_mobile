package cvut.fit.kot.data.model

data class SpecialistResponse(
    val id: Long,
    val firstName: String?,
    val lastName: String?,
    val email: String,
    val phoneNumber: String?,
    val description: String?,
    val averageRating: Double,
    val serviceOfferings: List<ServiceOfferingDto>,
    val location: LocationDto
)
