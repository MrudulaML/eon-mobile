package `in`.bitspilani.eon.login.data

import com.google.gson.annotations.SerializedName

data class CommonResponse(

    @SerializedName("message") val message: String
)

data class BasicDetailsResponse(
    var `data`: UserInfo
)

data class UserInfo(
    var address: String,
    var contact_number: String,
    var created_on: String,
    var id: Int,
    var interest: List<Int>? =null,
    var name: String?,
    var organization: String?,
    var role: Int,
    var updated_on: String,
    var user: Int
)