package cvut.fit.kot.data.model

data class OrderDto (
    val id: Long,

    val clientId: Long,

    val description: String,

    val price: Int,

    val location: LocationDto,

    val createAt: String,

    val serviceOfferings: List<ServiceOfferingDto>,

    val status: OrderStatus
)