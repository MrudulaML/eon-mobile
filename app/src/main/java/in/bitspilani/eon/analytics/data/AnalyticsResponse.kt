package `in`.bitspilani.eon.analytics.data


data class AnalyticsResponse(
    var `data`: Data,
    var message: String
)

data class Data(
    var cancelled_events: Int,
    var completed_events: Int,
    var event_list: ArrayList<OrganizedEvent>,
    var monthly_event_count: ArrayList<MonthlyEventCount>,
    var monthly_revenue: ArrayList<Int>,
    var ongoing_events: Int,
    var revenue_cancelled_events: Int,
    var revenue_completed_events: Int,
    var revenue_ongoing_events: Int,
    var ticket_graph_object: TicketGraphObject,
    var total_events: Int,
    var total_revenue: Int
)

data class OrganizedEvent(
    var key: Int,
    var location: String,
    var name: String,
    var revenue: Int,
    var sold_tickets: Int,
    var status: String,
    var total_tickets: Int
)

data class MonthlyEventCount(
    var `data`: List<Int>,
    var name: String
)

data class TicketGraphObject(
    var name_list: ArrayList<String>,
    var remaining_tickets: ArrayList<Int>,
    var revenue_list: ArrayList<Int>,
    var sold_tickets: ArrayList<Int>
)



