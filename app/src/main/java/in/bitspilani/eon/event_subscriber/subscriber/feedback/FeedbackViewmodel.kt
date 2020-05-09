package `in`.bitspilani.eon.event_subscriber.subscriber.feedback

import `in`.bitspilani.eon.BaseViewModel
import `in`.bitspilani.eon.api.ApiService
import `in`.bitspilani.eon.api.RestClient
import `in`.bitspilani.eon.event_subscriber.models.FeedbackBody
import `in`.bitspilani.eon.event_subscriber.models.PaymentResponse
import `in`.bitspilani.eon.event_subscriber.models.PresignResponse
import `in`.bitspilani.eon.event_subscriber.models.QuestionsResponse
import `in`.bitspilani.eon.event_subscriber.subscriber.payments.PaymentViewModel
import `in`.bitspilani.eon.login.data.CommonResponse
import `in`.bitspilani.eon.login.data.LoginResponse
import `in`.bitspilani.eon.utils.ApiCallback
import `in`.bitspilani.eon.utils.getViewModelFactory
import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FeedbackViewmodel(private val apiService: ApiService) : BaseViewModel() {

    private val restClient: RestClient = RestClient()

    var questionsData: MutableLiveData<QuestionsResponse> = MutableLiveData()

    var postFeedbackData: MutableLiveData<Boolean> = MutableLiveData()

    var presignData: MutableLiveData<PresignResponse> = MutableLiveData()
    var errorData: MutableLiveData<String> = MutableLiveData()

    var uploadImageData: MutableLiveData<Boolean> = MutableLiveData()

    fun getQuestions() {

        showProgress(true)

        apiService.getQuestions().enqueue(object : ApiCallback<QuestionsResponse>() {
            override fun onSuccessResponse(responseBody: QuestionsResponse) {

                showProgress(false)

                questionsData.postValue(responseBody)
            }

            override fun onApiError(errorType: ApiError, error: String?) {
                showProgress(false)

                errorData.postValue(error)
            }
        })

    }


    fun getPresignUrl(path_name: String) {

        var hashMap: HashMap<String, Any> = HashMap()

        //this is the image name basically such as test.jpg
        hashMap.put("path_name", path_name)


        showProgress(true)
        apiService.getPresignUrl(hashMap).enqueue(object : ApiCallback<PresignResponse>() {
            override fun onSuccessResponse(responseBody: PresignResponse) {

                showProgress(false)

                presignData.postValue(responseBody)
            }

            override fun onApiError(errorType: ApiError, error: String?) {
                showProgress(false)

                errorData.postValue(error)
            }
        })

    }

    fun uploadImageToS3(url: String, requestBody: RequestBody) {
        showProgress(true)


        restClient.noAuthClient.create(ApiService::class.java)
            .uploadImage(url, requestBody)
            .enqueue(object : Callback<Void> {

                override fun onResponse(
                    call: Call<Void>, response: Response<Void>
                ) {
                    showProgress(false)

                    if (response.isSuccessful) {

                        uploadImageData.postValue(true)
                    } else {

                        errorData.postValue(response.message())

                        Log.e("xoxo","image upload error onresponse: "+response.toString())


                    }

                }

                override fun onFailure(call: Call<Void>, t: Throwable) {

                    showProgress(false)

                    errorData.postValue(t.message)

                    Log.e("xoxo","image upload error: "+t.toString())


                }
            })





    }

    fun postFeedback(feedbackBody: FeedbackBody) {

        showProgress(true)

        apiService.postFeedback(feedbackBody).enqueue(object : ApiCallback<CommonResponse>() {
            override fun onSuccessResponse(responseBody: CommonResponse) {

                showProgress(false)

                postFeedbackData.postValue(true)
            }

            override fun onApiError(errorType: ApiError, error: String?) {
                showProgress(false)

                errorData.postValue(error)
            }
        })


    }


}