package `in`.bitspilani.eon.event_organiser.viewmodel


import `in`.bitspilani.eon.BaseViewModel
import `in`.bitspilani.eon.api.ApiService
import `in`.bitspilani.eon.api.RestClient
import `in`.bitspilani.eon.event_organiser.models.EventList
import `in`.bitspilani.eon.event_organiser.models.EventResponse
import `in`.bitspilani.eon.event_organiser.models.FilterResponse

import `in`.bitspilani.eon.utils.ApiCallback
import `in`.bitspilani.eon.utils.Constants
import `in`.bitspilani.eon.utils.ModelPreferencesManager
import `in`.bitspilani.eon.utils.SingleLiveEvent
import androidx.lifecycle.MutableLiveData
import timber.log.Timber

/**
 * This is a shared view model, all the shared view model will have zero con arguments.
 *
 */

class EventDashboardViewModel : BaseViewModel() {

    val eventDetailsObservables: MutableLiveData<EventList> =
        MutableLiveData()

    private val restClient: RestClient = RestClient()

    val eventClickObservable: SingleLiveEvent<Int> = SingleLiveEvent()
    fun getEvents(
        eventType: Int? = null,
        eventLocation: String? = null,
        startDate: String? = null,
        endDate: String? = null,
        fromFilter: Boolean = false,
        eventStatus:String?=null,
        subscriptionType:String?=null

    ) {
        showProgress(true)
        setupEventTypes()
        restClient.authClient.create(ApiService::class.java).getEvents(eventType, eventLocation, startDate, endDate,eventStatus,subscriptionType)
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

    fun setupEventTypes() {
        RestClient().authClient.create(ApiService::class.java).getFilter()
            .enqueue(object : ApiCallback<FilterResponse>(){
                override fun onSuccessResponse(responseBody: FilterResponse) {

                    ModelPreferencesManager.put(responseBody, Constants.EVENT_TYPES)

                }

                override fun onApiError(errorType: ApiError, error: String?) {

                }
            })
    }

    fun onEventClick(eventId: Int) {
        Timber.e("event clicked id$eventId")
        eventClickObservable.postValue(eventId)
    }


}