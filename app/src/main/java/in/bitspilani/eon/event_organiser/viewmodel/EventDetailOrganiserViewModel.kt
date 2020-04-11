package `in`.bitspilani.eon.event_organiser.viewmodel

import `in`.bitspilani.eon.api.ApiService
import `in`.bitspilani.eon.event_organiser.models.DetailResponseOrganiser
import `in`.bitspilani.eon.event_organiser.models.EventList
import `in`.bitspilani.eon.event_organiser.models.EventResponse
import `in`.bitspilani.eon.event_subscriber.models.EventDetailResponse
import `in`.bitspilani.eon.utils.ApiCallback
import `in`.bitspilani.eon.utils.SingleLiveEvent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventDetailOrganiserViewModel(private val apiService: ApiService) : ViewModel() {

    var eventData: SingleLiveEvent<DetailResponseOrganiser> = SingleLiveEvent()

    var errorView: SingleLiveEvent<String> = SingleLiveEvent()


    fun getEventDetails(id: Int) {

        apiService.getEventDetailsOrganiser(id)
            .enqueue(object : ApiCallback<DetailResponseOrganiser>() {
                override fun onSuccessResponse(responseBody: DetailResponseOrganiser) {
                    eventData.postValue(responseBody)
                }

                override fun onApiError(errorType: ApiError, error: String?) {
                    /*progress.value=false
                    errorView.postValue(error)*/
                }
            })

    }


}
