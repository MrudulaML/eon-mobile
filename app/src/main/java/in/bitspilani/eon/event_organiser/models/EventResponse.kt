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
    var event_status: String,
    var subscription_fee: Int,
    var time: String,
    var is_wishlisted: Boolean? = null,
    var is_subscribed: Boolean? = false
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

data class SelectedFilter(
    val eventType: Int?=null,
    val eventStatus: Int?=null,
    val eventFees: Int?=null,
    val startDate: String?=null,
    val endDate: String?=null,
    val byMe : Boolean? =null)