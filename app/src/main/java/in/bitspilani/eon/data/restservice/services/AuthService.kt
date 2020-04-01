package `in`.bitspilani.eon.data.restservice.services

import `in`.bitspilani.eon.data.restservice.models.LoginResponse
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {

    //will be changed after getting api contract
    @POST("/api/v1/core/user/login/")
    fun validateUser(@Body body: JsonObject): Call<LoginResponse>

    @POST("/api/v1/core/user/resetPassword")
    fun resetPassword(@Body body: JsonObject): Call<JsonObject>

}