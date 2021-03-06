package `in`.bitspilani.eon.event_organiser.models

data class DetailResponseOrganiser(
    var `data`: Data,
    var message: String
)

data class Data(
    var date: String,
    var description: String,
    var event_status: String,
    var event_type: Int,
    var external_links: String,
    var feedback_count: Int,
    var id: Int,
    var images: String,
    var invitee_list: ArrayList<Invitee>,
    var location: String,
    var name: String,
    var no_of_tickets: Int,
    var self_organised: Boolean,
    var sold_tickets: Int,
    var subscription_fee: Int,
    var time: String
)

data class Invitee(
    var discount_percentage: Int,
    var email: String,
    var invitation_id: Int,
    var user: User,
    var selectAll : Boolean? =false
)

data class User(
    var address: String,
    var contact_number: String,
    var name: String,
    var organization: String,
    var user_id: Int
)





