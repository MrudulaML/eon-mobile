package `in`.bitspilani.eon.event_organiser.models

//TODO club all the responses together remove this redundant data
data class AddInviteeResponse(
    var `data`: AddInviteeData,
    var message: String
)

data class AddInviteeData(
    var invitee_list: List<InviteeAfterAdd>
)

data class InviteeAfterAdd(
    var discount_percentage: Int,
    var email: String,
    var event: Event,
    var invitation_id: Int
)

data class EventAfterAdd(
    var event_id: Int,
    var event_name: String,
    var event_type: String
)