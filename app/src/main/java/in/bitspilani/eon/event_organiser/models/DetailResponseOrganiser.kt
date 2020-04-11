package `in`.bitspilani.eon.event_organiser.models

data class DetailResponseOrganiser(
    var `data`: List<Data>,
    var message: String
)

data class Data(
    var date: String,
    var description: String,
    var event_type: String,
    var external_links: String,
    var id: Int,
    var images: String,
    var invitee_list: List<Invitee>,
    var location: String,
    var name: String,
    var no_of_tickets: Int,
    var sold_tickets: Int,
    var subscription_fee: Int,
    var time: String
)

data class Invitee(
    var discount_percentage: Int,
    var email: String,
    var event: Event,
    var invitation_id: Int
)

data class Event(
    var event_id: Int,
    var event_name: String
)


