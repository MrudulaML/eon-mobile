package `in`.bitspilani.eon.event.subscriber.detail

import `in`.bitspilani.eon.api.ApiService
import `in`.bitspilani.eon.event.models.EventDetailResponse
import `in`.bitspilani.eon.utils.ApiCallback
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventDetailsViewModel(private val apiService: ApiService) : ViewModel() {

    var eventData: MutableLiveData<EventDetailResponse> = MutableLiveData()

    var errorView: MutableLiveData<String> = MutableLiveData()


    fun getEventDetails(id: Int) {

        apiService.getEventDetails(id).enqueue(object : Callback<EventDetailResponse> {

            override fun onResponse(
                call: Call<EventDetailResponse>,
                response: Response<EventDetailResponse>
            ) {

                if (response.isSuccessful) {

                    eventData.postValue(response.body())
                }

            }

            override fun onFailure(call: Call<EventDetailResponse>, t: Throwable) {

                errorView.postValue(t.message)
            }
        })
    }


}
