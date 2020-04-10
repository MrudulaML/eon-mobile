package `in`.bitspilani.eon.api

import `in`.bitspilani.eon.eventOrganiser.data.EventResponse
import `in`.bitspilani.eon.eventOrganiser.data.FilterResponse
import `in`.bitspilani.eon.login.data.GenerateCodeResponse
import `in`.bitspilani.eon.login.data.LoginResponse
import `in`.bitspilani.eon.login.data.ResetPasswordResponse
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    //Login
    @POST("/authentication/login")
    fun login(@Body body: JsonObject): Call<LoginResponse>

    @POST("authentication/reset-password")
    fun resetPassword(@Body body: JsonObject): Call<ResetPasswordResponse>

    @POST("authentication/generate-code")
    fun generateCode(@Body body: JsonObject): Call<GenerateCodeResponse>

    @POST("authentication/change-password")
    fun changePassword(@Body body: JsonObject): Call<GenerateCodeResponse>

    @POST("/api/v1/core/user/resetPassword")
    fun registerUser(@Body body: JsonObject): Call<JsonObject>

    /**
     * this end point will be used for getting events with filter types
     */

    @GET("/core/event/")
    fun getEvents(
        @Query("event_type") eventType: Int? = null,
        @Query("location") eventLocation: String? = null,
        @Query("start_date") startDate: String? = null,
        @Query("end_date") endDate: String? = null
    ): Call<EventResponse>

    @GET("/core/event")
    fun getEventFilter(): Call<FilterResponse>

}