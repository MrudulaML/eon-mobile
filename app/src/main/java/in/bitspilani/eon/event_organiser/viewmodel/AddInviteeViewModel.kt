package `in`.bitspilani.eon.event_organiser.viewmodel

import `in`.bitspilani.eon.api.ApiService
import `in`.bitspilani.eon.event_organiser.models.AddInviteeResponse
import `in`.bitspilani.eon.utils.ApiCallback
import `in`.bitspilani.eon.utils.SingleLiveEvent
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject

class AddInviteeViewModel(private val apiService: ApiService): ViewModel() {

    val addInviteeLiveData : SingleLiveEvent<AddInviteeResponse> = SingleLiveEvent()

    fun adInvitee(event_id:Int, discount:Int,emailList:List<String>){
        val body = JsonObject()
        body.addProperty("event",event_id)
        body.addProperty("discount_percentage",discount)
        body.addProperty("invitee_list",emailList.toString())
       // progress.value = true
        apiService.addInvitees(body)
            .enqueue(object : ApiCallback<AddInviteeResponse>(){
                override fun onSuccessResponse(responseBody: AddInviteeResponse) {
                    addInviteeLiveData.postValue(responseBody)
                }

                override fun onApiError(errorType: ApiError, error: String?) {
                    /*progress.value=false
                    errorView.postValue(error)*/
                }
            })

    }
}