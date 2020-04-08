package `in`.bitspilani.eon.eventOrganiser.data

data class EventResponse(val eventList: List<IndividualEvent>)


data class IndividualEvent(
    val eventName: String,
    val eventId: Int,
    val attendees: String
)
