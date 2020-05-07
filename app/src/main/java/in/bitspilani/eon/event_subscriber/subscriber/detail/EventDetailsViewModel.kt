package `in`.bitspilani.eon.event_subscriber.subscriber.detail

import `in`.bitspilani.eon.BaseViewModel
import `in`.bitspilani.eon.api.ApiService
import `in`.bitspilani.eon.event_subscriber.models.EventDetailResponse
import `in`.bitspilani.eon.event_subscriber.models.PaymentResponse
import `in`.bitspilani.eon.login.data.CommonResponse
import `in`.bitspilani.eon.login.data.SignUpResponse
import `in`.bitspilani.eon.utils.ApiCallback
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventDetailsViewModel(private val apiService: ApiService) : BaseViewModel() {

    var eventData: MutableLiveData<EventDetailResponse> = MutableLiveData()

    var errorToast: MutableLiveData<String> = MutableLiveData()

    var shareEmailError: MutableLiveData<String> = MutableLiveData()

    var wishlistData: MutableLiveData<String> = MutableLiveData()

    var emailApiData: MutableLiveData<String> = MutableLiveData()

    var cancelEventData: MutableLiveData<String> = MutableLiveData()

    var eventId: Int? = 0

    fun getEventDetails(id: Int) {

        showProgress(true)

        apiService.getEventDetails(id).enqueue(object : Callback<EventDetailResponse> {

            override fun onResponse(
                call: Call<EventDetailResponse>,
                response: Response<EventDetailResponse>
            ) {


                showProgress(false)
                if (response.isSuccessful) {


                    eventData.postValue(response.body())
                    eventId = response.body()?.data?.event_id
                }

            }

            override fun onFailure(call: Call<EventDetailResponse>, t: Throwable) {


                showProgress(false)
                errorToast.postValue(t.message)
            }
        })
    }

    var wishlist: Boolean = true

    fun addOrRemovetoWishlist() {

        if (wishlist) {

            wishlistEvent()
        } else {

            removeFromWishlist()
        }
    }


    fun wishlistEvent() {

        showProgress(true)

        var hashMap: HashMap<String, Any> = HashMap()

        hashMap.put("event_id", eventId as Any)

        apiService.wishlist(hashMap).enqueue(object : Callback<CommonResponse> {

            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                showProgress(false)

                if (response.isSuccessful) {

                    wishlist = false
                    wishlistData.postValue(response.body()?.message)
                } else {

                    if (response.body()?.message != null)
                        errorToast.postValue(response.body()?.message)

                }

            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {

                errorToast.postValue(t.message)
            }
        })
    }

    fun removeFromWishlist() {

        showProgress(true)
        apiService.deleteWishlist(eventId).enqueue(object : Callback<CommonResponse> {

            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {

                showProgress(false)

                if (response.isSuccessful) {

                    wishlist = true
                    wishlistData.postValue(response.body()?.message)
                } else {

                    if (response.body()?.message != null)
                        errorToast.postValue(response.body()?.message)
                }

            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {

                showProgress(false)
                errorToast.postValue(t.message)
            }
        })

    }


    fun sendEmail(map: HashMap<String, Any>) {

        showProgress(true)

        apiService.sendEmail(map).enqueue(object : ApiCallback<CommonResponse>() {
            override fun onSuccessResponse(response: CommonResponse) {

                showProgress(false)
                emailApiData.postValue(response.message)
            }

            override fun onApiError(errorType: ApiError, error: String?) {
                showProgress(false)
                errorView.postValue(error)
            }
        })
    }

    var freeEventLiveData: MutableLiveData<PaymentResponse> = MutableLiveData()

    fun subscribeToFreeEvent(hashMap: HashMap<String, Any>) {

        showProgress(true)

        apiService.subscribeEvent(hashMap)
            .enqueue(object : ApiCallback<PaymentResponse>() {
                override fun onSuccessResponse(response: PaymentResponse) {

                    showProgress(false)
                    freeEventLiveData.postValue(response)
                }

                override fun onApiError(errorType: ApiError, error: String?) {
                    showProgress(false)
                    errorToast.postValue(error)
                }
            })
    }

    fun cancelEvent(eventId: Int) {

        apiService.cancelEvent(eventId)
            .enqueue(object : Callback<CommonResponse> {

                override fun onResponse(
                    call: Call<CommonResponse>,
                    response: Response<CommonResponse>
                ) {

                    if (response.isSuccessful) {

                        cancelEventData.postValue(response.body()!!.message)

                    } else {

                        if (response.body()?.message != null)
                            errorToast.postValue(response.body()?.message)
                    }

                }

                override fun onFailure(call: Call<CommonResponse>, t: Throwable) {

                    errorToast.postValue(t.message)
                }
            })
    }

}
