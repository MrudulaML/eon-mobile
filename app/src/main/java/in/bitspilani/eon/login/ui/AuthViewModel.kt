package `in`.bitspilani.eon.login.ui

import `in`.bitspilani.eon.data.remote.ApiService
import `in`.bitspilani.eon.data.remote.RestClient
import `in`.bitspilani.eon.login.data.LoginResponse
import `in`.bitspilani.eon.utils.ApiCallback
import `in`.bitspilani.eon.utils.SingleLiveEvent
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject

enum class OrganiserDetailsSteps(val desc: String) {
    BASIC_DETAILS("Basic Details"),
    BANK_DETAILS("Bank Details"),
    PASSWORD("Password"),
}

enum class USER_TYPE(val desc: String) {
    ORGANISER("Event Organiser"),
    SUBSCRIBER("Event Subscriber"),
}

class AuthViewModel: ViewModel() {

    val progress: SingleLiveEvent<Boolean> = SingleLiveEvent()
    var registerCurrentStep: OrganiserDetailsSteps =
        OrganiserDetailsSteps.BASIC_DETAILS
    var userType: USER_TYPE? = null
    val restClient: RestClient = RestClient()

    var fcmToken:String? = null


    fun login(username:String, password:String, userType:String){
        val body = JsonObject()
        body.addProperty("username",username)
        body.addProperty("password",password)
        body.addProperty("user_type", userType)
        progress.value = true
        restClient.authClient.create(ApiService::class.java).validateUser(body)
            .enqueue(object : ApiCallback<LoginResponse>(){
                override fun onSuccessResponse(responseBody: LoginResponse) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onApiError(errorType: ApiError, error: String?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }
            })

    }


    fun register(username:String, password:String){
        val body = JsonObject()
        body.addProperty("username",username)
        body.addProperty("password",password)
        progress.value = true
        restClient.authClient.create(ApiService::class.java).validateUser(body)
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