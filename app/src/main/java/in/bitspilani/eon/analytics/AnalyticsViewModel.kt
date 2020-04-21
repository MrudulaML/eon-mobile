package `in`.bitspilani.eon.analytics

import `in`.bitspilani.eon.BaseViewModel
import `in`.bitspilani.eon.analytics.data.AnalyticsResponse
import `in`.bitspilani.eon.api.ApiService
import `in`.bitspilani.eon.utils.ApiCallback
import `in`.bitspilani.eon.utils.SingleLiveEvent


class AnalyticsViewModel(val apiService: ApiService) : BaseViewModel() {

    val analyticsLiveData : SingleLiveEvent<AnalyticsResponse> = SingleLiveEvent()

    fun getAnalytics(){
        showProgress(true)
        apiService.getAnalytics()
            .enqueue(object : ApiCallback<AnalyticsResponse>(){
                override fun onSuccessResponse(responseBody: AnalyticsResponse) {
                    analyticsLiveData.postValue(responseBody)
                    showProgress(false)
                }

                override fun onApiError(errorType: ApiError, error: String?) {
                    errorView.postValue(error)
                    showProgress(false)
                }
            })

    }

}