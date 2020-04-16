package `in`.bitspilani.eon.login.data

import com.google.gson.annotations.SerializedName

data class SignupRequest (

    @SerializedName("email") val email : String,
    @SerializedName("password") val password : String,
    @SerializedName("contact") val contact : Int,
    @SerializedName("organization") val organization : String,
    @SerializedName("address") val address : String,
    @SerializedName("role") val role : String
)