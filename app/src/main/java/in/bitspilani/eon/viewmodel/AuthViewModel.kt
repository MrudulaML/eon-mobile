package `in`.bitspilani.eon.viewmodel

import `in`.bitspilani.eon.data.restservice.RestClient
import `in`.bitspilani.eon.data.restservice.models.LoginResponse
import `in`.bitspilani.eon.data.restservice.services.AuthService
import `in`.bitspilani.eon.utils.ApiCallback
import `in`.bitspilani.eon.utils.SingleLiveEvent
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject

class AuthViewModel: ViewModel() {

    val progress: SingleLiveEvent<Boolean> = SingleLiveEvent()
    val restClient = RestClient()

    var fcmToken:String? = null


    fun login(username:String, password:String, userType:String){
        val body = JsonObject()
        body.addProperty("username",username)
        body.addProperty("password",password)
        body.addProperty("user_type", userType)
        progress.value = true
        restClient.authClient.create(AuthService::class.java).validateUser(body)
            .enqueue(object : ApiCallback<LoginResponse>(){
                override fun onSuccessResponse(responseBody: LoginResponse) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onApiError(errorType: ApiError, error: String?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }
            })

    }
}