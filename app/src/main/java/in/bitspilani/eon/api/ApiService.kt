package `in`.bitspilani.eon.api

import `in`.bitspilani.eon.eventOrganiser.data.EventResponse
import `in`.bitspilani.eon.login.data.GenerateCodeResponse
import `in`.bitspilani.eon.login.data.LoginResponse
import `in`.bitspilani.eon.login.data.ResetPasswordResponse
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {


    //Login
    @POST("/authentication/login")
    fun login(@Body body: JsonObject): Call<LoginResponse>

    @POST("authentication/reset-password")
    fun resetPassword(@Body body: JsonObject): Call<ResetPasswordResponse>

    @POST("authentication/generate-code")
    fun generateCode(@Body body: JsonObject): Call<GenerateCodeResponse>


    @POST("/api/v1/core/user/resetPassword")
    fun registerUser(@Body body: JsonObject): Call<JsonObject>

    @GET("/api/v1/core/user/events")
    suspend fun getEvents(): Response<EventResponse>

}