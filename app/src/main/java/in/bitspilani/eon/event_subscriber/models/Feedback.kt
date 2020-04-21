package `in`.bitspilani.eon.event_subscriber.models

import android.net.Uri

data class Feedback(var question: String) {

    var questionNumber: Int = 0
    var imageUri: Uri? = null
    var answer: String? = null
}