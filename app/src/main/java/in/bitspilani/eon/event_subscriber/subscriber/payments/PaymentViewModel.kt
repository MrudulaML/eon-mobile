package `in`.bitspilani.eon.event_subscriber.subscriber.payments

import `in`.bitspilani.eon.api.ApiService
import `in`.bitspilani.eon.event_subscriber.models.PaymentResponse
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PaymentViewModel(private val apiService: ApiService) : ViewModel() {


    var subscriptionData: MutableLiveData<PaymentResponse> = MutableLiveData()

    var errorData: MutableLiveData<String> = MutableLiveData()


    fun payAndSubscribe(hashMap: HashMap<String, Any>) {

        apiService.subscribeEvent(hashMap).enqueue(object : Callback<PaymentResponse> {

            override fun onResponse(
                call: Call<PaymentResponse>,
                response: Response<PaymentResponse>
            ) {

                if (response.isSuccessful) {

                    subscriptionData.postValue(response.body())
                } else {

                    errorData.postValue(response.message())
                }

            }

            override fun onFailure(call: Call<PaymentResponse>, t: Throwable) {


            }
        })

    }


}
