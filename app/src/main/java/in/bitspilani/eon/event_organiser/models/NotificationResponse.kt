package `in`.bitspilani.eon.event_organiser.models

data class NotificationResponse(
    var `data`: List<Notification>
)

data class Notification(
    var created_on: String,
    var event: String,
    var event_id: Int,
    var id: Int,
    var message: String
)
