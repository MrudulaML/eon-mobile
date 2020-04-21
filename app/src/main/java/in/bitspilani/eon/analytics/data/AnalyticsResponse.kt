package `in`.bitspilani.eon.analytics.data


data class AnalyticsResponse(
    var `data`: Data,
    var message: String
)

data class Data(
    var cancelled_events: Int,
    var completed_events: Int,
    var event_list: ArrayList<OrganizedEvent>,
    var ongoing_events: Int,
    var revenue_cancelled_events: Int,
    var revenue_completed_events: Int,
    var revenue_ongoing_events: Int,
    var total_events: Int,
    var total_revenue: Int
)

data class OrganizedEvent(
    var id: Int,
    var name: String,
    var revenue: Int,
    var sold_tickets: Int,
    var status: String,
    var total_tickets: Int
)