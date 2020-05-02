package `in`.bitspilani.eon.event_organiser.viewmodel

import `in`.bitspilani.eon.BaseViewModel
import `in`.bitspilani.eon.api.ApiService
import `in`.bitspilani.eon.login.data.BasicDetailsResponse
import `in`.bitspilani.eon.login.data.CommonResponse
import `in`.bitspilani.eon.utils.ApiCallback
import `in`.bitspilani.eon.utils.SingleLiveEvent
import com.google.gson.JsonArray
import com.google.gson.JsonObject

class UserProfileViewModel(val apiService: ApiService) : BaseViewModel() {

     var basicDetailUpdateLiveData: SingleLiveEvent<BasicDetailsResponse> = SingleLiveEvent()
    var basicDetailGetLiveData: SingleLiveEvent<BasicDetailsResponse> = SingleLiveEvent()


    fun updateUserDetails(
        user_id: Int,
        name: String?=null,
        contact: String?=null,
        organization: String? =null,
        address:String ? = null,
        role: Int? = null,
        interestList:JsonArray? =null
    ) {
        val body = JsonObject()

        body.addProperty("id", user_id)
        name?.let { body.addProperty("name", it) }
        contact?.let { body.addProperty("contact_number", it) }
        organization?.let { body.addProperty("organization", it) }
        address?.let { body.addProperty("address", it) }
        role?.let { body.addProperty("role", it) }
        interestList?.let { body.add("interest", it)}

        showProgress(true)
        apiService.updateBasicDetails(user_id,body)
            .enqueue(object : ApiCallback<BasicDetailsResponse>() {
                override fun onSuccessResponse(responseBody: BasicDetailsResponse) {
                    basicDetailUpdateLiveData.postValue(responseBody)
                    showProgress(false)
                }

                override fun onApiError(errorType: ApiError, error: String?) {
                    /*progress.value=false
                    errorView.postValue(error)*/
                    showProgress(false)
                }
            })

    }


    fun getUserDetails(id: Int){

        showProgress(true)
        apiService.getUserDetail(id)
            .enqueue(object : ApiCallback<BasicDetailsResponse>() {
                override fun onSuccessResponse(responseBody: BasicDetailsResponse) {
                    basicDetailGetLiveData.postValue(responseBody)
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