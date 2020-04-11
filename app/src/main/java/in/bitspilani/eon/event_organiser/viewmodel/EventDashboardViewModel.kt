package `in`.bitspilani.eon.event_organiser.viewmodel


import `in`.bitspilani.eon.api.ApiService
import `in`.bitspilani.eon.api.RestClient
import `in`.bitspilani.eon.event_organiser.models.EventList
import `in`.bitspilani.eon.event_organiser.models.EventResponse

import `in`.bitspilani.eon.utils.ApiCallback
import `in`.bitspilani.eon.utils.SingleLiveEvent
import androidx.lifecycle.ViewModel
import timber.log.Timber

class EventDashboardViewModel : ViewModel() {

    val eventDetailsObservables: SingleLiveEvent<EventList> =
        SingleLiveEvent()

    private val restClient: RestClient = RestClient()

    val eventClickObservable: SingleLiveEvent<Int> = SingleLiveEvent()
    fun getEvents(
        eventType: Int? = null,
        eventLocation: String? = null,
        startDate: String? = null,
        endDate: String? = null,
        fromFilter: Boolean = false
    ) {

        restClient.authClient.create(ApiService::class.java).getEvents(eventType, eventLocation, startDate, endDate)
            .enqueue(object : ApiCallback<EventResponse>() {
                override fun onSuccessResponse(responseBody: EventResponse) {
                    val eventList = EventList(fromFilter,responseBody.data)
                    eventDetailsObservables.postValue(eventList)

                }

                override fun onApiError(errorType: ApiError, error: String?) {
                    /*progress.value=false
                    errorView.postValue(error)*/
                }
            })

    }



    fun onEventClick(eventId: Int) {

        eventClickObservable.postValue(eventId)
    }


}