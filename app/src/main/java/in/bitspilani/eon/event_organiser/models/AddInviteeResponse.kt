package `in`.bitspilani.eon.event_organiser.models

//TODO club all the responses together remove this redundant data
data class AddInviteeResponse(
    var `data`: AddInviteeData,
    var message: String
)

data class AddInviteeData(
    var invitee_list: ArrayList<Invitee>
)
