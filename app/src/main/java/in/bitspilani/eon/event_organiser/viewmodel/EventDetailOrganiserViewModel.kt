package `in`.bitspilani.eon.event_organiser.viewmodel

import `in`.bitspilani.eon.BaseViewModel
import `in`.bitspilani.eon.api.ApiService
import `in`.bitspilani.eon.event_organiser.models.DetailResponseOrganiser
import `in`.bitspilani.eon.event_organiser.models.EventList
import `in`.bitspilani.eon.event_organiser.models.EventResponse
import `in`.bitspilani.eon.event_subscriber.models.EventDetailResponse
import `in`.bitspilani.eon.login.data.CommonResponse
import `in`.bitspilani.eon.utils.ApiCallback
import `in`.bitspilani.eon.utils.SingleLiveEvent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventDetailOrganiserViewModel(private val apiService: ApiService) : BaseViewModel() {

    var eventData: SingleLiveEvent<DetailResponseOrganiser> = SingleLiveEvent()

    var notifyLiveData: SingleLiveEvent<CommonResponse> = SingleLiveEvent()



    fun getEventDetails(id: Int) {
        showProgress(true)
        apiService.getEventDetailsOrganiser(id)
            .enqueue(object : ApiCallback<DetailResponseOrganiser>() {
                override fun onSuccessResponse(responseBody: DetailResponseOrganiser) {
                    eventData.postValue(responseBody)
                    showProgress(false)
                }

                override fun onApiError(errorType: ApiError, error: String?) {
                    /*progress.value=false
                    errorView.postValue(error)*/
                    showProgress(false)
                }
            })

    }

    fun notifySubscriber(type: String,event_id:Int,message:String) {
        showProgress(true)
        val body = JsonObject()
        body.addProperty("event_id",event_id)
        body.addProperty("message",message)
        body.addProperty("type",type)
        apiService.notifySubscriber(body)
//            .enqueue(object : ApiCallback<CommonResponse>() {
//                override fun onSuccessResponse(responseBody: CommonResponse) {
//                    notifyLiveData.postValue(responseBody)
//                    showProgress(false)
//                }
//
//                override fun onApiError(errorType: ApiError, error: String?) {
//                    /*progress.value=false
//                    errorView.postValue(error)*/
//                    showProgress(false)
//                }
//            })

    }


}
