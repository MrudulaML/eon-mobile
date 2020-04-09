package `in`.bitspilani.eon.login.data

import com.google.gson.annotations.SerializedName

public data class SignUpResponse (

    @SerializedName("data") val data : Data,
    @SerializedName("message") val message : String
)

 data class Data (

    @SerializedName("access") val access : String,
    @SerializedName("refresh") val refresh : String,
    @SerializedName("user") val user : User
)

data class Role (

    @SerializedName("id") val id : Int,
    @SerializedName("role") val role : String
)

data class User (

    @SerializedName("user_id") val user_id : Int,
    @SerializedName("email") val email : String,
    @SerializedName("active_status") val active_status : Boolean,
    @SerializedName("name") val name : String,
    @SerializedName("created_on") val created_on : String,
    @SerializedName("updated_on") val updated_on : String,
    @SerializedName("contact_number") val contact_number : Int,
    @SerializedName("organization") val organization : String,
    @SerializedName("address") val address : String,
    @SerializedName("role") val role : Role
)