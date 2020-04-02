package `in`.bitspilani.eon.data.restservice.services

import `in`.bitspilani.eon.data.restservice.models.IndividualEvent
import `in`.bitspilani.eon.data.restservice.models.LoginResponse
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthService {

    //will be changed after getting api contract
    @POST("/api/v1/core/user/login/")
    fun validateUser(@Body body: JsonObject): Call<LoginResponse>

    @POST("/api/v1/core/user/resetPassword")
    fun resetPassword(@Body body: JsonObject): Call<JsonObject>

    @POST("/api/v1/core/user/resetPassword")
    fun registerUser(@Body body: JsonObject): Call<JsonObject>

    @GET("/api/v1/core/user/events")
    fun getEvents(): Call<IndividualEvent>

}