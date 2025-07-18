package cvut.fit.kot.data.model

data class OrderResponse (
    val id: Long,

    val clientId: Long,

    val title: String,

    val description: String,

    val price: Int,

    val location: LocationDto,

    val createAt: String,

    val serviceOfferings: List<ServiceOfferingDto>,

    val status: OrderStatus
)