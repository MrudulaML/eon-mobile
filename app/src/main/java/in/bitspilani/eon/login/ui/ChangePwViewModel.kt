package `in`.bitspilani.eon.login.ui

import `in`.bitspilani.eon.BaseViewModel
import `in`.bitspilani.eon.api.ApiService
import `in`.bitspilani.eon.login.data.CommonResponse
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
            .enqueue(object : Callback<CommonResponse> {
                override fun onResponse(
                    call: Call<CommonResponse>,
                    response: Response<CommonResponse>
                ) {
                    showProgress(false)

                    if(response.isSuccessful){

                        changePasswordMsg.postValue(response.body()!!.message)

                    }
                    else{
                        errorToast.postValue(response.body()!!.message)
                    }


                }

                override fun onFailure(call: Call<CommonResponse>, t: Throwable) {

                    showProgress(false)

                    changePasswordMsg.postValue(t.message)



                }
            })

    }

}