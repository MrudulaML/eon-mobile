package `in`.bitspilani.eon.viewmodel



import `in`.bitspilani.eon.data.restservice.RestClient
import `in`.bitspilani.eon.data.restservice.models.IndividualEvent
import `in`.bitspilani.eon.data.restservice.models.LoginResponse
import `in`.bitspilani.eon.data.restservice.services.AuthService
import `in`.bitspilani.eon.utils.ApiCallback
import `in`.bitspilani.eon.utils.SingleLiveEvent
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject

class HomeViewModel(private val restClient: RestClient): ViewModel() {

    val progress: SingleLiveEvent<Boolean> = SingleLiveEvent()
    var fcmToken:String? = null


    fun getEvents(){
        progress.value = true
        restClient.authClient.create(AuthService::class.java).getEvents()
            .enqueue(object : ApiCallback<IndividualEvent>(){
                override fun onSuccessResponse(responseBody: IndividualEvent) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onApiError(errorType: ApiError, error: String?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }
            })

    }
}