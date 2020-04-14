package `in`.bitspilani.eon.utils

object Constants {

    const val PREF_CONST = "BITS_EON_PREF"

    //pref keys
    const val CURRENT_USER = "user_data"
    const val EVENT_TYPES = "event_types"
    const val ACCESS_TOKEN = "access_token"
    const val REFRESH_TOKEN = "refresh_token"
    //1 for organiser and 1 for subscriber
    const val USER_ROLE = "user_role"


}

enum class UserType(val type: Int) {
    ORGANISER(1),
    SUBSCRIBER(2)
}
