package `in`.bitspilani.eon.event_organiser.viewmodel

import `in`.bitspilani.eon.BaseViewModel
import `in`.bitspilani.eon.api.ApiService
import `in`.bitspilani.eon.event_organiser.models.FeedbackData
import `in`.bitspilani.eon.event_organiser.models.FeedbackListResponse
import `in`.bitspilani.eon.event_organiser.models.Responses
import `in`.bitspilani.eon.event_subscriber.models.PaymentResponse
import `in`.bitspilani.eon.event_subscriber.models.QuestionsResponse
import `in`.bitspilani.eon.utils.ApiCallback
import `in`.bitspilani.eon.utils.Constants
import `in`.bitspilani.eon.utils.SingleLiveEvent
import android.provider.SyncStateContract
import android.util.Log
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrgFeedbackViewmodel(private val apiService: ApiService) : BaseViewModel() {


    var url: MutableLiveData<String> = MutableLiveData()

    var feedbackListData: SingleLiveEvent<FeedbackListResponse> = SingleLiveEvent()

    var errorToast: MutableLiveData<String> = MutableLiveData()

    fun getUsers(eventId: Int) {

        showProgress(true)

        var hashMap: HashMap<String,Any> = HashMap()

        hashMap.put(Constants.EVENT_ID,eventId)

        apiService.getFeedbackList(eventId)
            .enqueue(object : ApiCallback<FeedbackListResponse>() {
                override fun onSuccessResponse(responseBody: FeedbackListResponse) {

                    showProgress(false)

                    feedbackListData.postValue(responseBody)
                }

                override fun onApiError(errorType: ApiError, error: String?) {
                    showProgress(false)

                    errorToast.postValue(error)

                }
            })


    }


    var detailPage: SingleLiveEvent<FeedbackData> = SingleLiveEvent()

    fun onSingleUserFeedbackClick(list:FeedbackData){

        detailPage.postValue(list)

    }



}