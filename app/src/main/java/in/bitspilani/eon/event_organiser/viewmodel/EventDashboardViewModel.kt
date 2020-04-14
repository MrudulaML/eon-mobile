package `in`.bitspilani.eon.event_organiser.viewmodel


import `in`.bitspilani.eon.BaseViewModel
import `in`.bitspilani.eon.api.ApiService
import `in`.bitspilani.eon.api.RestClient
import `in`.bitspilani.eon.event_organiser.models.EventList
import `in`.bitspilani.eon.event_organiser.models.EventResponse

import `in`.bitspilani.eon.utils.ApiCallback
import `in`.bitspilani.eon.utils.SingleLiveEvent
import timber.log.Timber

/**
 * This is a shared view model, all the shared view model will have zero con arguments.
 *
 */

class EventDashboardViewModel : BaseViewModel() {

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
        showProgress(true)
        restClient.authClient.create(ApiService::class.java).getEvents(eventType, eventLocation, startDate, endDate)
            .enqueue(object : ApiCallback<EventResponse>() {
                override fun onSuccessResponse(responseBody: EventResponse) {
                    val eventList = EventList(fromFilter,responseBody.data)
                    eventDetailsObservables.postValue(eventList)
                    showProgress(false)
                }

                override fun onApiError(errorType: ApiError, error: String?) {
                    /*progress.value=false

                    errorView.postValue(error)*/
                    showProgress(false)
                }
            })

    }



    fun onEventClick(eventId: Int) {
        Timber.e("event clicked id$eventId")
        eventClickObservable.postValue(eventId)
    }


}