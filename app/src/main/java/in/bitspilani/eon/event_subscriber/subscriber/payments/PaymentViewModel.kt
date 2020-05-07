package `in`.bitspilani.eon.event_subscriber.subscriber.payments

import `in`.bitspilani.eon.BaseViewModel
import `in`.bitspilani.eon.api.ApiService
import `in`.bitspilani.eon.event_subscriber.models.PaymentResponse
import `in`.bitspilani.eon.login.data.SignUpResponse
import `in`.bitspilani.eon.utils.ApiCallback
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PaymentViewModel(private val apiService: ApiService) : BaseViewModel() {


    var subscriptionData: MutableLiveData<PaymentResponse> = MutableLiveData()

    var errorData: MutableLiveData<String> = MutableLiveData()


    fun payAndSubscribe(hashMap: HashMap<String, Any>) {

        showProgress(true)

        apiService.subscribeEvent(hashMap)
            .enqueue(object : ApiCallback<PaymentResponse>(){
                override fun onSuccessResponse(response: PaymentResponse) {

                    showProgress(false)
                    subscriptionData.postValue(response)
                }

                override fun onApiError(errorType: ApiError, error: String?) {
                    showProgress(false)
                    errorData.postValue(error)
                }
            })

    }


}
