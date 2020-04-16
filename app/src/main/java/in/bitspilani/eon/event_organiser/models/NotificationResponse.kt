package `in`.bitspilani.eon.event_organiser.models

data class NotificationResponse(
    var `data`: List<Notification>
)

data class Notification(
    var id: Int,
    var message: String
)