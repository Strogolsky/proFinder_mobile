package cvut.fit.kot.data.model

data class OrderRequest(

    val title: String,

    val description: String,

    val price: Int,

    val location: LocationDto,

    val serviceOfferings: List<ServiceOfferingDto>,
)
