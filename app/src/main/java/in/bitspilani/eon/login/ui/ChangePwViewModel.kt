package `in`.bitspilani.eon.login.ui

import `in`.bitspilani.eon.BaseViewModel
import `in`.bitspilani.eon.api.ApiService
import `in`.bitspilani.eon.event_subscriber.models.PaymentResponse
import `in`.bitspilani.eon.login.data.CommonResponse
import `in`.bitspilani.eon.utils.ApiCallback
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangePwViewModel(private val apiService: ApiService) : BaseViewModel() {

    var changePasswordMsg: MutableLiveData<String> = MutableLiveData()

    var errorToast : MutableLiveData<String> = MutableLiveData()

    fun changePassword(hashMap: HashMap<String, Any>) {

        showProgress(true)

        apiService.changePassword(hashMap)
            .enqueue(object : ApiCallback<CommonResponse>(){
                override fun onSuccessResponse(response: CommonResponse) {

                    showProgress(false)
                    changePasswordMsg.postValue(response.message)
                }

                override fun onApiError(errorType: ApiError, error: String?) {
                    showProgress(false)
                    errorToast.postValue(error)
                }
            })


    }

}