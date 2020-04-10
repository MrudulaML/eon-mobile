package `in`.bitspilani.eon.eventOrganiser.data


class FilterResponse(
    var `data`: List<EventType>
)

data class EventType(
    var id: Int,
    var is_active: Boolean,
    var type: String
)

data class EventList(
    val fromFilter: Boolean = false,
    val eventList: List<MonoEvent>
)