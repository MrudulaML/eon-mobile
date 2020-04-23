package `in`.bitspilani.eon.api

import `in`.bitspilani.eon.analytics.data.AnalyticsResponse
import `in`.bitspilani.eon.event_organiser.models.*
import `in`.bitspilani.eon.event_subscriber.models.*
import `in`.bitspilani.eon.login.data.*
import com.google.gson.JsonObject
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

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
        @Query("end_date") endDate: String? = null,
        @Query("event_status") eventStatus: String? = null,
        @Query("subscription_type") subType: String? = null
    ): Call<EventResponse>

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

    @POST("/core/notify-subscriber")
    fun notifySubscriber(@Body body: JsonObject): Call<CommonResponse>

    //to wishlist an event
    @POST("core/wishlist/")
    fun wishlist(@Body map: HashMap<String, Any>): Call<CommonResponse>

    //to remove event from wishlist
    @DELETE("core/wishlist/{id}/")
    fun deleteWishlist(@Path("id") id: Int?): Call<CommonResponse>

    //to send email
    @POST("core/share-with-friend/")
    fun sendEmail(@Body map: HashMap<String, Any>): Call<CommonResponse>

    //to pay and subscribe to an event
    @POST("core/subscription/")
    fun subscribeEvent(@Body map: HashMap<String, Any>): Call<PaymentResponse>

    @DELETE("core/subscription/{id}/")
    fun cancelEvent(@Path("id") id: Int?): Call<CommonResponse>

    @GET("/core/notification/")
    fun getNotification(): Call<NotificationResponse>

    @HTTP(method = "DELETE", path = "/core/invite/", hasBody = true)
    fun deleteInvitee(@Body body: JsonObject): Call<CommonResponse>

    @PATCH("/core/notification/")
    fun getNotificationRead(@Body body: HashMap<String, Any>): Call<CommonResponse>

    @GET("/core/event-summary/")
    fun getAnalytics(@Query("event_status") event_status: String): Call<AnalyticsResponse>

    @PATCH("core/user/{user_id}/")
    fun updateBasicDetails(@Path("user_id") user_id: Int, @Body jsonObject: JsonObject): Call<BasicDetailsResponse>

    @GET("core/feedback-questions/")
    fun getQuestions(): Call<QuestionsResponse>

    @POST("core/presigned-url/")
    fun getPresignUrl(@Body body: HashMap<String, Any>): Call<PresignResponse>

    @POST("core/feedback/")
    fun postFeedback(@Body feedbackBody: FeedbackBody): Call<CommonResponse>

    @PUT
    fun uploadImage(@Url url: String, @Body image: RequestBody): Call<Void>

    @GET("core/feedback/")
    fun getFeedbackList(@Query("event_id") id: Int):Call<FeedbackListResponse>

}
