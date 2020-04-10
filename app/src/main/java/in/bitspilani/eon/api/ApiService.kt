package `in`.bitspilani.eon.api

import `in`.bitspilani.eon.eventOrganiser.data.EventResponse
import `in`.bitspilani.eon.login.data.*
import com.google.android.gms.common.internal.service.Common
import com.google.gson.JsonObject
import io.reactivex.Observable
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

    @POST("authentication/change-password")
    fun changePassword(@Body body: JsonObject): Call<GenerateCodeResponse>

    @POST("/authentication/registration")
    fun registerUser(@Body map: HashMap<String, Any>): Call<SignUpResponse>

    @GET("/api/v1/core/user/events")
    suspend fun getEvents(): Response<EventResponse>

    @GET("/authentication/change-password")
    fun changePassword(@Body map: HashMap<String, Any>): Call<CommonResponse>

}