package `in`.bitspilani.eon.login.ui

import `in`.bitspilani.eon.api.ApiService
import `in`.bitspilani.eon.login.data.CommonResponse
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangePwViewModel(private val apiService: ApiService) : ViewModel() {

    var changePasswordMsg: MutableLiveData<String> = MutableLiveData()


    fun changePassword(hashMap: HashMap<String, Any>) {
        apiService.changePassword(hashMap)
            .enqueue(object : Callback<CommonResponse> {
                override fun onResponse(
                    call: Call<CommonResponse>,
                    response: Response<CommonResponse>
                ) {

                    changePasswordMsg.postValue(response.body()?.message)

                    Log.e("xoxo", "register success")
                }

                override fun onFailure(call: Call<CommonResponse>, t: Throwable) {

                    changePasswordMsg.postValue(t.message)

                    Log.e("xoxo", "register error: " + t.toString())

                }
            })

    }

}