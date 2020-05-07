package `in`.bitspilani.eon.event_subscriber.models

import android.net.Uri
import com.google.gson.annotations.SerializedName

data class QuestionsResponse(

    @SerializedName("data") val data: ArrayList<FeedbackData>,
    @SerializedName("message") val message: String
)

data class FeedbackData(

    var questionNumber: Int = 0,
    var imageUri: Uri? = null,

    @SerializedName("id") val id: Int = 0,
    @SerializedName("question") val question: String? = null,
    @SerializedName("answer") var answer: Answer? = Answer()

)

data class Answer(

    @SerializedName("description") var description: String? = null,
    @SerializedName("image") var image: String? = null
)

data class FeedbackBody(

    @SerializedName("event_id") val id: Int,
    @SerializedName("feedback") val feedback: ArrayList<FeedbackData>
)

data class PresignResponse(

    @SerializedName("data") val data: PresignData

)


data class PresignData(

    @SerializedName("presigned_url") val presigned_url: String,
    @SerializedName("image_name") val image_name: String
)