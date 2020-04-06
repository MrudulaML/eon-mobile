package `in`.bitspilani.eon.api

import `in`.bitspilani.eon.eventOrganiser.data.EventResponse
import `in`.bitspilani.eon.login.data.LoginResponse
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    //will be changed after getting api contract
    @POST("/api/v1/core/user/login/")
    fun validateUser(@Body body: JsonObject): Call<LoginResponse>

    @POST("/api/v1/core/user/resetPassword")
    fun resetPassword(@Body body: JsonObject): Call<JsonObject>

    @POST("/api/v1/core/user/resetPassword")
    fun registerUser(@Body body: JsonObject): Call<JsonObject>

    @GET("/api/v1/core/user/events")
    suspend fun getEvents(): Response<EventResponse>

}