package `in`.bitspilani.eon.event_subscriber.models

import com.google.gson.annotations.SerializedName

data class PaymentResponse(

    @SerializedName("data") val data: Data,
    @SerializedName("message") val message: String
)

data class PaymentData(

    @SerializedName("curent_payment_id") val curent_payment_id: Int,
    @SerializedName("current_payment_ref_number") val current_payment_ref_number: String,
    @SerializedName("no_of_tickets") val no_of_tickets: Int,
    @SerializedName("amount") val amount: Int,
    @SerializedName("discount_amount") val discount_amount: Int,
    @SerializedName("total_amount") val total_amount: Int,
    @SerializedName("event_name") val event_name: String,
    @SerializedName("event_date") val event_date: String,
    @SerializedName("event_time") val event_time: String,
    @SerializedName("event_location") val event_location: String
)