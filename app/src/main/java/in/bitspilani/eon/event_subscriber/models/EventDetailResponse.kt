package `in`.bitspilani.eon.event_subscriber.models

import com.google.gson.annotations.SerializedName
import java.util.function.BinaryOperator


data class EventDetailResponse(

    @SerializedName("data") val data: Data,
    @SerializedName("message") val message: String
)


data class Data(

    @SerializedName("id") val event_id: Int,
    @SerializedName("name") val event_name: String,
    @SerializedName("date") val date: String,
    @SerializedName("time") val time: String,
    @SerializedName("discount_percentage") val discountPercentage: Int,
    @SerializedName("location") val location: String,
    @SerializedName("event_type") val event_type: String,
    @SerializedName("description") val description: String,
    @SerializedName("is_subscribed") val is_subscribed: Boolean = false,
    @SerializedName("is_wishlisted") val is_wishlisted: Boolean = false,
    @SerializedName("subscription_fee") val subscription_fee: Int,
    @SerializedName("images") val images: String,
    @SerializedName("external_links") val external_links: String,
    @SerializedName("invitee_list") val invitee_list: List<Invitee_list>,
    @SerializedName("subscription_details") val subscription_details: Subscription_details? = null
)

data class Invitee_list(

    @SerializedName("invitation_id") val invitation_id: Int,
    @SerializedName("email") val email: String,
    @SerializedName("event") val event: Event,
    @SerializedName("discount_percentage") val discount_percentage: Int
)

data class Event(

    @SerializedName("event_id") val event_id: Int,
    @SerializedName("event_name") val event_name: String
)

data class Subscription_details(


    @SerializedName("id") val id: Int = 0,
    @SerializedName("no_of_tickets_bought") val no_of_tickets_bought: Int = 0,
    @SerializedName("amount_paid") val amount_paid: Int,
    @SerializedName("discount_given") val discount_given: Int
)