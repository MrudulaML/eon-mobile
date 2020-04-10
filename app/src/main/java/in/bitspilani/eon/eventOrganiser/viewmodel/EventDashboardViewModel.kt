package `in`.bitspilani.eon.eventOrganiser.viewmodel


import `in`.bitspilani.eon.api.ApiService
import `in`.bitspilani.eon.eventOrganiser.data.EventList
import `in`.bitspilani.eon.eventOrganiser.data.EventResponse

import `in`.bitspilani.eon.eventOrganiser.data.MonoEvent
import `in`.bitspilani.eon.utils.ApiCallback
import `in`.bitspilani.eon.utils.SingleLiveEvent
import androidx.lifecycle.ViewModel
import timber.log.Timber

class EventDashboardViewModel(private val apiService: ApiService) : ViewModel() {

    val eventDetailsObservables: SingleLiveEvent<EventList> =
        SingleLiveEvent()

    val eventClickObservable: SingleLiveEvent<Int> = SingleLiveEvent()
    fun getEvents(
        eventType: Int? = null,
        eventLocation: String? = null,
        startDate: String? = null,
        endDate: String? = null,
        fromFilter: Boolean = false
    ) {

        apiService.getEvents(eventType, eventLocation, startDate, endDate)
            .enqueue(object : ApiCallback<EventResponse>() {
                override fun onSuccessResponse(responseBody: EventResponse) {
                    val eventList = EventList(fromFilter,responseBody.data)
                    eventDetailsObservables.postValue(eventList)
                    Timber.e(responseBody.data[0].name)
                }

                override fun onApiError(errorType: ApiError, error: String?) {
                    /*progress.value=false
                    errorView.postValue(error)*/
                }
            })

    }

    /*  fun getFilter(){

          apiService.filter()
              .enqueue(object : ApiCallback<EventResponse>(){
                  override fun onSuccessResponse(responseBody: EventResponse) {
                      eventDetails.postValue(responseBody.data)
                      Timber.e(responseBody.data[0].name)
                  }

                  override fun onApiError(errorType: ApiError, error: String?) {
                      *//*progress.value=false
                    errorView.postValue(error)*//*
                }
            })

    }*/

    fun onEventClick(eventId: Int) {

        eventClickObservable.postValue(eventId)
    }


}