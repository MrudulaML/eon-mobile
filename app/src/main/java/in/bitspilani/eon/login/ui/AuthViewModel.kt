package `in`.bitspilani.eon.login.ui

import `in`.bitspilani.eon.api.ApiService
import `in`.bitspilani.eon.api.RestClient
import `in`.bitspilani.eon.login.data.*
import `in`.bitspilani.eon.utils.ApiCallback
import `in`.bitspilani.eon.utils.SingleLiveEvent
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.custom.asyncResult
import java.lang.Exception

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

class AuthViewModel : ViewModel() {

    val progress: SingleLiveEvent<Boolean> = SingleLiveEvent()
    var registerCurrentStep: OrganiserDetailsSteps =
        OrganiserDetailsSteps.BASIC_DETAILS

    var forgotPasswordSteps: ForgotPasswordSteps = ForgotPasswordSteps.ENTER_DETAILS
    var userType: USER_TYPE? = null
    val restClient: RestClient = RestClient()

    var registerData: MutableLiveData<Data> = MutableLiveData()
    var registerError: MutableLiveData<String> = MutableLiveData()

    var fcmToken: String? = null

    lateinit var generatedCode: String
    val loginLiveData: SingleLiveEvent<LoginResponse> = SingleLiveEvent()
    val generateCodeLiveData: SingleLiveEvent<GenerateCodeResponse> = SingleLiveEvent()
    val resetPasswordLiveData: SingleLiveEvent<ResetPasswordResponse> = SingleLiveEvent()


    fun login(username: String, password: String) {
        val body = JsonObject()
        body.addProperty("username", username)
        body.addProperty("password", password)
        progress.value = true
        restClient.authClient.create(ApiService::class.java).login(body)
            .enqueue(object : ApiCallback<LoginResponse>() {
                override fun onSuccessResponse(responseBody: LoginResponse) {
                    loginLiveData.postValue(responseBody)
                }

                override fun onApiError(errorType: ApiError, error: String?) {
                    //handle errors
                }
            })

    }

    fun generateCode(email: String) {
        val body = JsonObject()
        body.addProperty("email", email)
        progress.value = true
        restClient.authClient.create(ApiService::class.java).generateCode(body)
            .enqueue(object : ApiCallback<GenerateCodeResponse>() {
                override fun onSuccessResponse(generateCodeResponse: GenerateCodeResponse) {
                    generateCodeLiveData.postValue(generateCodeResponse)
                }

                override fun onApiError(errorType: ApiError, error: String?) {
                    //handle errors
                }
            })

    }

    fun resetPassword(email: String, code: String, password: String) {
        val body = JsonObject()
        body.addProperty("email", email)
        body.addProperty("code", code)
        body.addProperty("password", password)
        progress.value = true
        restClient.authClient.create(ApiService::class.java).resetPassword(body)
            .enqueue(object : ApiCallback<ResetPasswordResponse>() {
                override fun onSuccessResponse(resetPasswordResponse: ResetPasswordResponse) {
                    resetPasswordLiveData.postValue(resetPasswordResponse)
                }

                override fun onApiError(errorType: ApiError, error: String?) {
                    //handle errors
                }
            })

    }

    fun register(hashMap: HashMap<String, Any>) {

        try {

            //progress.value = true
            restClient.authClient.create(ApiService::class.java)
                .registerUser(hashMap)
                .enqueue(object : ApiCallback<SignUpResponse>() {
                    override fun onSuccessResponse(signUpResponse: SignUpResponse) {

                        registerData.postValue(signUpResponse.data)

                        Log.e("xoxo","register success")

                    }

                    override fun onApiError(errorType: ApiError, error: String?) {
                        //handle errors
                        Log.e("xoxo","register error: "+errorType+" "+error)
                        registerError.postValue(error)

                    }
                })
        }catch (e:Exception){

            Log.e("xoxo","register error: "+e.toString())

        }
    }

}