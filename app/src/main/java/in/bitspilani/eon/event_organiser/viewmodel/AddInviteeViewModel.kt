package `in`.bitspilani.eon.event_organiser.viewmodel

import `in`.bitspilani.eon.BaseViewModel
import `in`.bitspilani.eon.api.ApiService
import `in`.bitspilani.eon.event_organiser.models.AddInviteeResponse
import `in`.bitspilani.eon.utils.ApiCallback
import `in`.bitspilani.eon.utils.SingleLiveEvent
import com.google.gson.JsonArray
import com.google.gson.JsonObject

class AddInviteeViewModel(private val apiService: ApiService): BaseViewModel() {

    val addInviteeLiveData : SingleLiveEvent<AddInviteeResponse> = SingleLiveEvent()

    fun adInvitee(event_id:Int, discount:Int?, emailList: JsonArray){
        val body = JsonObject()
        body.addProperty("event",event_id)
        discount?.let {
            body.addProperty("discount_percentage",discount)
        }
        body.add("invitee_list",emailList)
        showProgress(true)
        apiService.addInvitees(body)
            .enqueue(object : ApiCallback<AddInviteeResponse>(){
                override fun onSuccessResponse(responseBody: AddInviteeResponse) {
                    addInviteeLiveData.postValue(responseBody)
                    showProgress(false)
                }

                override fun onApiError(errorType: ApiError, error: String?) {
                    showProgress(false)
                }
            })

    }


}