package `in`.bitspilani.eon.event_organiser.viewmodel

import `in`.bitspilani.eon.api.ApiService
import `in`.bitspilani.eon.event_organiser.models.EventType
import `in`.bitspilani.eon.event_organiser.models.FilterResponse
import `in`.bitspilani.eon.utils.ApiCallback
import `in`.bitspilani.eon.utils.SingleLiveEvent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class EventFilterViewModel(val apiService: ApiService) : ViewModel(){

    val eventFilterObservable: SingleLiveEvent<List<EventType>> =
        SingleLiveEvent()

    fun getFilter(){

        apiService.getFilter()
            .enqueue(object : ApiCallback<FilterResponse>(){
                override fun onSuccessResponse(responseBody: FilterResponse) {
                    eventFilterObservable.postValue(responseBody.data)

                }

                override fun onApiError(errorType: ApiError, error: String?) {
                    /*  progress.value=false
                    errorView.postValue(error)*/
                }
            })

    }
}