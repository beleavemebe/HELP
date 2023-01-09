package company.vk.education.siriusapp.data.api.gis

data class GisAddressResponse(
    val result: Result
)

data class Result(
    val items: List<Item>,
)

data class Item(
    val name: String,
)
