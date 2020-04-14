package `in`.bitspilani.eon.event_subscriber.subscriber.summary

import `in`.bitspilani.eon.api.ApiService
import `in`.bitspilani.eon.event_subscriber.models.PaymentResponse
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventSummaryViewModel(private val apiService: ApiService) : ViewModel() {

    var subcriptionData: MutableLiveData<PaymentResponse> = MutableLiveData()

    fun reduceTickets(map: HashMap<String, Any>) {

        apiService.subscribeEvent(map)
            .enqueue(object : Callback<PaymentResponse> {

                override fun onResponse(
                    call: Call<PaymentResponse>,
                    response: Response<PaymentResponse>
                ) {

                    if (response.isSuccessful) {

                        subcriptionData.postValue(response.body())
                    } else {

                    }

                }

                override fun onFailure(call: Call<PaymentResponse>, t: Throwable) {


                }
            })

    }
}
