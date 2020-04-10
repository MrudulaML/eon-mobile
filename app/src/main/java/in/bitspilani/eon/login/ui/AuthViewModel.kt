package `in`.bitspilani.eon.login.ui

import `in`.bitspilani.eon.api.ApiService
import `in`.bitspilani.eon.api.RestClient
import `in`.bitspilani.eon.login.data.GenerateCodeResponse
import `in`.bitspilani.eon.login.data.LoginResponse
import `in`.bitspilani.eon.login.data.ResetPasswordResponse
import `in`.bitspilani.eon.utils.ApiCallback
import `in`.bitspilani.eon.utils.SingleLiveEvent
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import org.jetbrains.anko.custom.asyncResult
import timber.log.Timber

enum class OrganiserDetailsSteps(val desc: String) {
    BASIC_DETAILS("Basic Details"),
    BANK_DETAILS("Bank Details"),
    PASSWORD("Password"),
}

enum class ForgotPasswordSteps(val desc: String) {
    ENTER_DETAILS("Enter Details"),
    VERIFICATION_CODE("Verification Code"),
    PASSWORD("Password"),
    SUCCESS("Success"),
}

enum class USER_TYPE(val desc: String) {
    ORGANISER("Event Organiser"),
    SUBSCRIBER("Event Subscriber"),
}

class AuthViewModel: ViewModel() {

    internal val progress: SingleLiveEvent<Boolean> = SingleLiveEvent()

    val errorView: SingleLiveEvent<String> = SingleLiveEvent()
    var registerCurrentStep: OrganiserDetailsSteps =
        OrganiserDetailsSteps.BASIC_DETAILS

    var forgotPasswordSteps: ForgotPasswordSteps = ForgotPasswordSteps.ENTER_DETAILS
    var userType: USER_TYPE? = null
    private val restClient: RestClient = RestClient()

    val loginLiveData: SingleLiveEvent<LoginResponse> = SingleLiveEvent()
    val generateCodeLiveData: SingleLiveEvent<GenerateCodeResponse> = SingleLiveEvent()
    val resetPasswordLiveData: SingleLiveEvent<ResetPasswordResponse> = SingleLiveEvent()


    fun login(username:String, password:String){
        val body = JsonObject()
        body.addProperty("email",username)
        body.addProperty("password",password)
        progress.value = true
        restClient.authClient.create(ApiService::class.java).login(body)
            .enqueue(object : ApiCallback<LoginResponse>(){
                override fun onSuccessResponse(responseBody: LoginResponse) {
                    loginLiveData.postValue(responseBody)
                }

                override fun onApiError(errorType: ApiError, error: String?) {
                    progress.value=false
                    errorView.postValue(error)
                }
            })

    }

    fun generateCode(email:String)
    {
        val body = JsonObject()
        body.addProperty("email",email)
        progress.value = true
        restClient.authClient.create(ApiService::class.java).generateCode(body)
            .enqueue(object : ApiCallback<GenerateCodeResponse>(){
                override fun onSuccessResponse(generateCodeResponse: GenerateCodeResponse) {
                    generateCodeLiveData.postValue(generateCodeResponse)
                }

                override fun onApiError(errorType: ApiError, error: String?) {
                    //handle errors
                }
            })

    }

    fun resetPassword(email:String,code:String,password:String)
    {
        val body = JsonObject()
        body.addProperty("email",email)
        body.addProperty("code",code)
        body.addProperty("password",password)
        progress.value = true
        restClient.authClient.create(ApiService::class.java).resetPassword(body)
            .enqueue(object : ApiCallback<ResetPasswordResponse>(){
                override fun onSuccessResponse(resetPasswordResponse: ResetPasswordResponse) {
                    resetPasswordLiveData.postValue(resetPasswordResponse)
                }

                override fun onApiError(errorType: ApiError, error: String?) {
                    //handle errors
                }
            })

    }

    fun register(username:String, password:String){
        val body = JsonObject()
        body.addProperty("username",username)
        body.addProperty("password",password)
        progress.value = true
       /* restClient.authClient.create(ApiService::class.java).validateUser(body)
            .enqueue(object : ApiCallback<LoginResponse>(){
                override fun onSuccessResponse(responseBody: LoginResponse) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onApiError(errorType: ApiError, error: String?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }
            })*/

    }


}