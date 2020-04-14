package `in`.bitspilani.eon.event_organiser.viewmodel

import `in`.bitspilani.eon.BaseViewModel
import `in`.bitspilani.eon.api.ApiService
import `in`.bitspilani.eon.event_organiser.models.EventType
import `in`.bitspilani.eon.event_organiser.models.FilterResponse
import `in`.bitspilani.eon.utils.ApiCallback
import `in`.bitspilani.eon.utils.Constants
import `in`.bitspilani.eon.utils.ModelPreferencesManager
import `in`.bitspilani.eon.utils.SingleLiveEvent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class EventFilterViewModel(private val apiService: ApiService) : BaseViewModel(){

    val eventFilterObservable: SingleLiveEvent<List<EventType>> =
        SingleLiveEvent()

    fun getFilter(){
        showProgress(true)
        apiService.getFilter()
            .enqueue(object : ApiCallback<FilterResponse>(){
                override fun onSuccessResponse(responseBody: FilterResponse) {
                    eventFilterObservable.postValue(responseBody.data)
                    //refresh events
                    ModelPreferencesManager.put(responseBody, Constants.EVENT_TYPES)
                    showProgress(false)

                }

                override fun onApiError(errorType: ApiError, error: String?) {
                    /*  progress.value=false
                    errorView.postValue(error)*/
                    showProgress(false)
                }
            })

    }
}