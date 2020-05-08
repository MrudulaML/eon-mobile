package `in`.bitspilani.eon.event_subscriber.subscriber.summary

import `in`.bitspilani.eon.BaseViewModel
import `in`.bitspilani.eon.api.ApiService
import `in`.bitspilani.eon.event_subscriber.models.PaymentResponse
import `in`.bitspilani.eon.login.data.LoginResponse
import `in`.bitspilani.eon.utils.ApiCallback
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventSummaryViewModel(private val apiService: ApiService) : BaseViewModel() {

    var subcriptionData: MutableLiveData<PaymentResponse> = MutableLiveData()

    var errorData: MutableLiveData<String> = MutableLiveData()

    fun reduceTickets(map: HashMap<String, Any>) {

        showProgress(true)

        apiService.subscribeEvent(map)
            .enqueue(object : ApiCallback<PaymentResponse>(){
                override fun onSuccessResponse(responseBody: PaymentResponse) {
                    showProgress(false)
                    subcriptionData.postValue(responseBody)
                }

                override fun onApiError(errorType: ApiError, error: String?) {
                    showProgress(false)
                    errorData.postValue(error)
                }
            })

    }


}
