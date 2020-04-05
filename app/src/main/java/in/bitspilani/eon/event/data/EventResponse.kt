package `in`.bitspilani.eon.event.data

data class EventResponse (val eventList :List<IndividualEvent>)


data class IndividualEvent(val eventName:String,
                      val attendees:String)
