package `in`.bitspilani.eon.eventOrganiser.viewmodel



import `in`.bitspilani.eon.api.ApiService
import `in`.bitspilani.eon.eventOrganiser.data.EventResponse

import `in`.bitspilani.eon.eventOrganiser.data.MonoEvent
import `in`.bitspilani.eon.utils.ApiCallback
import `in`.bitspilani.eon.utils.SingleLiveEvent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import timber.log.Timber

class EventDetailsViewModel(private val apiService: ApiService): ViewModel() {

    val eventDetails: SingleLiveEvent<List<MonoEvent>> =
        SingleLiveEvent<List<MonoEvent>>()

    val eventId : MutableLiveData<String> = MutableLiveData()
    fun getEvents(){

        apiService.getEvents()
            .enqueue(object : ApiCallback<EventResponse>(){
                override fun onSuccessResponse(responseBody: EventResponse) {
                    eventDetails.postValue(responseBody.data)
                    Timber.e(responseBody.data[0].name)
                }

                override fun onApiError(errorType: ApiError, error: String?) {
                    /*progress.value=false
                    errorView.postValue(error)*/
                }
            })

    }

    fun onEventClick( eventId:Int ){

       // eventDetails.postValue("details")
    }



}