package `in`.bitspilani.eon.event_organiser.models

import com.google.gson.annotations.SerializedName

data class FeedbackListResponse (

    @SerializedName("data") val data : ArrayList<FeedbackData>,
    @SerializedName("message") val message : String
)

data class Responses (

    @SerializedName("question_id") val question_id : Int,
    @SerializedName("question") val question : String,
    @SerializedName("answer") val answer : String,
    @SerializedName("image") val image : String
)

data class FeedbackUser (

    @SerializedName("id") val id : Int,
    @SerializedName("email") val email : String,
    @SerializedName("contact") val contact : Long,
    @SerializedName("name") val name : String
)
data class FeedbackData (

    @SerializedName("user") val user : FeedbackUser,
    @SerializedName("responses") val responses : List<Responses>
)