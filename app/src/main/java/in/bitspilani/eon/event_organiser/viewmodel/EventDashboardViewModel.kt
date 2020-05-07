package `in`.bitspilani.eon.event_organiser.viewmodel


import `in`.bitspilani.eon.BaseViewModel
import `in`.bitspilani.eon.api.ApiService
import `in`.bitspilani.eon.api.RestClient
import `in`.bitspilani.eon.event_organiser.models.*

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

    val eventFilterObservable: MutableLiveData<List<EventType>> =
        SingleLiveEvent()
    val selectedFilterObservable: MutableLiveData<SelectedFilter> =
        SingleLiveEvent()

    private val restClient: RestClient = RestClient()

    val eventClickObservable: SingleLiveEvent<Int> = SingleLiveEvent()
    fun getEvents(
        eventType: Int? = null,
        eventLocation: String? = null,
        startDate: String? = null,
        endDate: String? = null,
        fromFilter: Boolean = false,
        eventStatus: String? = null,
        subscriptionType: String? = null,
        isByMe: String? = null

    ) {
        showProgress(true)
        setupEventTypes()
        restClient.authClient.create(ApiService::class.java).getEvents(
                eventType,
                eventLocation,
                startDate,
                endDate,
                eventStatus,
                subscriptionType,
                isByMe
            )
            .enqueue(object : ApiCallback<EventResponse>() {
                override fun onSuccessResponse(responseBody: EventResponse) {
                    val eventList = EventList(fromFilter, responseBody.data)
                    eventDetailsObservables.postValue(eventList)
                    showProgress(false)
                }

                override fun onApiError(errorType: ApiError, error: String?) {
                    errorView.postValue(error)
                    showProgress(false)
                }
            })

    }

    fun setupEventTypes() {
        RestClient().authClient.create(ApiService::class.java).getFilter()
            .enqueue(object : ApiCallback<FilterResponse>() {
                override fun onSuccessResponse(responseBody: FilterResponse) {

                    ModelPreferencesManager.put(responseBody, Constants.EVENT_TYPES)

                    eventFilterObservable.postValue(responseBody.data)
                    //refresh events
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


    private var preSelectedFilters = SelectedFilter()
    fun getSelectedFilters() {
        selectedFilterObservable.postValue(preSelectedFilters)
    }

    fun setSelectedFilter(selectedFilter: SelectedFilter) {
        preSelectedFilters = selectedFilter
    }

    fun resetFilters() {
        preSelectedFilters = SelectedFilter()
    }

}