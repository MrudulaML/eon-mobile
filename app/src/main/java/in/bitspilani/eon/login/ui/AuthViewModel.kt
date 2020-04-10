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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
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

class AuthViewModel : ViewModel() {

    internal val progress: SingleLiveEvent<Boolean> = SingleLiveEvent()

    val errorView: SingleLiveEvent<String> = SingleLiveEvent()
    var registerCurrentStep: OrganiserDetailsSteps =
        OrganiserDetailsSteps.BASIC_DETAILS

    var forgotPasswordSteps: ForgotPasswordSteps = ForgotPasswordSteps.ENTER_DETAILS
    var userType: USER_TYPE? = null
    private val restClient: RestClient = RestClient()

    var registerData: SingleLiveEvent<Data> = SingleLiveEvent()
    var registerError: MutableLiveData<String> = MutableLiveData()

    var fcmToken: String? = null

    lateinit var generatedCode: String
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

    fun register(hashMap: HashMap<String, Any>) {

        try {

            restClient.authClient.create(ApiService::class.java).registerUser(hashMap)
                .enqueue(object : Callback<SignUpResponse> {
                    override fun onResponse(
                        call: Call<SignUpResponse>,
                        response: Response<SignUpResponse>
                    ) {

                        registerData.postValue(response.body()?.data)

                        Log.e("xoxo", "register success")
                    }

                    override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                        Log.e("xoxo", "register error: " + t.toString())
                        registerError.postValue(t.toString())
                    }
                })
        } catch (e: Exception) {

            Log.e("xoxo", "register error: " + e.toString())

        }
    }




}