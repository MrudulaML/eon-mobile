package `in`.bitspilani.eon.event_organiser.models

data class AddInviteeResponse(
    var `data`: AddInviteeData,
    var message: String
)

data class AddInviteeData(
    var invitee_list: ArrayList<Invitee>
)
