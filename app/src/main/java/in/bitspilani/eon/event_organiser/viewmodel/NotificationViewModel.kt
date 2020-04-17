package `in`.bitspilani.eon.event_organiser.viewmodel

import `in`.bitspilani.eon.BaseViewModel
import `in`.bitspilani.eon.api.ApiService
import `in`.bitspilani.eon.event_organiser.models.Notification
import `in`.bitspilani.eon.event_organiser.models.NotificationResponse
import `in`.bitspilani.eon.event_subscriber.models.EventDetailResponse
import `in`.bitspilani.eon.login.data.CommonResponse
import `in`.bitspilani.eon.utils.ApiCallback
import `in`.bitspilani.eon.utils.SingleLiveEvent
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificationViewModel(private val apiService: ApiService) : BaseViewModel() {


    var notificationList: List<Notification> = ArrayList<Notification>()

    val clearAllData:MutableLiveData<CommonResponse> = MutableLiveData()

    val notificationLiveData: SingleLiveEvent<NotificationResponse> = SingleLiveEvent()

    fun getNotification() {

        apiService.getNotification()
            .enqueue(object : ApiCallback<NotificationResponse>() {
                override fun onSuccessResponse(responseBody: NotificationResponse) {

                    notificationList=responseBody.data
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

    fun readNotification() {

        var notificationIdList: ArrayList<Int> = ArrayList<Int>()

        showProgress(true)

        if(notificationList!=null){

            notificationList.forEach {

                notificationIdList.add(it.id)

            }


        }

        var hashMap : HashMap<String,Any> = HashMap()
        hashMap.put("notification_ids",notificationIdList)

        apiService.getNotificationRead(hashMap)
            .enqueue(object : Callback<CommonResponse> {

                override fun onResponse(call: Call<CommonResponse>,
                    response: Response<CommonResponse>) {

                    showProgress(false)
                    if (response.isSuccessful) {

                        clearAllData.postValue(response.body())

                    }

                }

                override fun onFailure(call: Call<CommonResponse>, t: Throwable) {


                }
            })

    }



}