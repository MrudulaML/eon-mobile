package `in`.bitspilani.eon.event_organiser.viewmodel

import `in`.bitspilani.eon.BaseViewModel
import `in`.bitspilani.eon.api.ApiService
import `in`.bitspilani.eon.event_organiser.models.FeedbackListResponse
import `in`.bitspilani.eon.event_subscriber.models.QuestionsResponse
import `in`.bitspilani.eon.utils.ApiCallback
import `in`.bitspilani.eon.utils.Constants
import android.provider.SyncStateContract
import androidx.lifecycle.MutableLiveData

class OrgFeedbackViewmodel(private val apiService: ApiService) : BaseViewModel() {


    var feedbackListData: MutableLiveData<FeedbackListResponse> = MutableLiveData()

    var errorToast: MutableLiveData<String> = MutableLiveData()

    fun getUsers(eventId: Int) {

        showProgress(true)

        apiService.getFeedbackList(hashMapOf(Constants.EVENT_ID to eventId))
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


}