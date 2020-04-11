package `in`.bitspilani.eon.event_organiser.models




data class EventResponse(
    var `data`: ArrayList<MonoEvent>,
    var message: String
)

data class MonoEvent(
    var date: String,
    var description: String,
    var event_type: String,
    var external_links: String,
    var id: Int,
    var images: String,
    var invitee_list: List<Any>,
    var location: String,
    var name: String,
    var no_of_tickets: Int,
    var sold_tickets: Int,
    var subscription_fee: Int,
    var time: String
)


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
    val eventList: ArrayList<MonoEvent>
)