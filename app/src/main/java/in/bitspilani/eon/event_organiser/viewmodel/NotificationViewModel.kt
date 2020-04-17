package `in`.bitspilani.eon.event_organiser.viewmodel

import `in`.bitspilani.eon.BaseViewModel
import `in`.bitspilani.eon.api.ApiService
import `in`.bitspilani.eon.event_organiser.models.NotificationResponse
import `in`.bitspilani.eon.utils.ApiCallback
import `in`.bitspilani.eon.utils.SingleLiveEvent

class NotificationViewModel(private val apiService: ApiService): BaseViewModel() {

    val notificationLiveData : SingleLiveEvent<NotificationResponse> = SingleLiveEvent()

    fun getNotification(){

        apiService.getNotification()
            .enqueue(object : ApiCallback<NotificationResponse>(){
                override fun onSuccessResponse(responseBody: NotificationResponse) {
                    notificationLiveData.postValue(responseBody)
                    showProgress(false)
                }

                override fun onApiError(errorType: ApiError, error: String?) {
                    /*progress.value=false
                    errorView.postValue(error)*/
                    showProgress(false)
                }
            })

    }
}