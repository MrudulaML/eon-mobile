package `in`.bitspilani.eon.event_organiser.viewmodel

import `in`.bitspilani.eon.BaseViewModel
import `in`.bitspilani.eon.api.ApiService
import `in`.bitspilani.eon.event_organiser.models.DetailResponseOrganiser
import `in`.bitspilani.eon.login.data.CommonResponse
import `in`.bitspilani.eon.utils.ApiCallback
import `in`.bitspilani.eon.utils.SingleLiveEvent
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonArray
import com.google.gson.JsonObject

class EventDetailOrganiserViewModel(private val apiService: ApiService) : BaseViewModel() {

    var eventData: SingleLiveEvent<DetailResponseOrganiser> = SingleLiveEvent()

    var notifyLiveData: SingleLiveEvent<CommonResponse> = SingleLiveEvent()

    var deleteInvitee: SingleLiveEvent<CommonResponse> = SingleLiveEvent()

    var deleteProgress: MutableLiveData<Boolean> = SingleLiveEvent()

    var startFeedback:  MutableLiveData<Int> = MutableLiveData()

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

    fun deleteInvitee(invitationId:List<Int>,eventId:Int) {
        val body = JsonObject()
        body.addProperty("event_id",eventId)
        val array = JsonArray()
        for (each in invitationId) {
            array.add(each)
        }
        body.add("invitation_ids",array)
        deleteProgress.postValue(true)
        apiService.deleteInvitee(body)
            .enqueue(object : ApiCallback<CommonResponse>() {
                override fun onSuccessResponse(responseBody: CommonResponse) {
                    deleteInvitee.postValue(responseBody)
                    deleteProgress.postValue(false)
                }

                override fun onApiError(errorType: ApiError, error: String?) {
                    /*progress.value=false
                    errorView.postValue(error)*/
                    deleteProgress.postValue(false)
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
            .enqueue(object : ApiCallback<CommonResponse>() {
                override fun onSuccessResponse(responseBody: CommonResponse) {
                    notifyLiveData.postValue(responseBody)
                    showProgress(false)
               }

                override fun onApiError(errorType: ApiError, error: String?) {

                    errorView.postValue(error)
                    showProgress(false)
                }
            })

    }


}
