package `in`.bitspilani.eon.utils

object Constants {

    const val PREF_CONST = "BITS_EON_PREF"

    //pref keys
    const val CURRENT_USER = "user_data"
    const val ACCESS_TOKEN = "access_token"
    const val REFRESH_TOKEN = "refresh_token"
    //1 for organiser and 1 for subscriber
    const val USER_ROLE = "user_role"

    const val EVENT_ID = "event_id"
    const val AMOUNT = "Amount"
    const val DISCOUNT_AMOUNT = "disc_amount"
    const val DISCOUNT_PERCENTAGE = "disc_percentage"
    const val IS_UPDATE = "is_update"
    const val UPDATED_ATTENDEES = "updated_attendees"
    const val ATTENDEES = "attendees"
    const val PROMOCODE="promocode"
   const val NUMBER_OF_TICKETS_BOUGHT="no_of_tickets_bought"
    const val NUMBER_OF_TICKETS="no_of_tickets"

    const val USER_ID="user_id"
}

enum class UserType(val type: Int) {
    ORGANISER(1),
    SUBSCRIBER(0)
}
