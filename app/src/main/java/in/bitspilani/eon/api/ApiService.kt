package `in`.bitspilani.eon.api

import `in`.bitspilani.eon.event_organiser.models.AddInviteeResponse
import `in`.bitspilani.eon.event_organiser.models.DetailResponseOrganiser
import `in`.bitspilani.eon.event_subscriber.models.EventDetailResponse
import `in`.bitspilani.eon.event_organiser.models.EventResponse
import `in`.bitspilani.eon.event_organiser.models.FilterResponse
import `in`.bitspilani.eon.login.data.GenerateCodeResponse
import `in`.bitspilani.eon.login.data.LoginResponse
import `in`.bitspilani.eon.login.data.ResetPasswordResponse
import `in`.bitspilani.eon.login.data.*
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Path

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

    @POST("/authentication/registration")
    fun registerUser(@Body map: HashMap<String, Any>): Call<SignUpResponse>

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

    @POST("/authentication/change-password")
    fun changePassword(@Body map: HashMap<String, Any>): Call<CommonResponse>


    @GET("/core/event/{id}")
    fun getEventDetails(@Path("id") id: Int): Call<EventDetailResponse>

    @GET("/core/event/{id}")
    fun getEventDetailsOrganiser(@Path("id") id: Int): Call<DetailResponseOrganiser>

    @GET("/core/event-type")
    fun getFilter(): Call<FilterResponse>

    @POST("/core/invite")
    fun addInvitees(@Body body: JsonObject): Call<AddInviteeResponse>
}