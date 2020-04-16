package `in`.bitspilani.eon.event_subscriber.subscriber.detail

import `in`.bitspilani.eon.BaseViewModel
import `in`.bitspilani.eon.api.ApiService
import `in`.bitspilani.eon.event_subscriber.models.EventDetailResponse
import `in`.bitspilani.eon.event_subscriber.models.PaymentResponse
import `in`.bitspilani.eon.login.data.CommonResponse
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


        var hashMap: HashMap<String, Any> = HashMap()

        hashMap.put("event_id", eventId as Any)

        apiService.wishlist(hashMap).enqueue(object : Callback<CommonResponse> {

            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {

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

        apiService.deleteWishlist(eventId).enqueue(object : Callback<CommonResponse> {

            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {

                if (response.isSuccessful) {

                    wishlist = true
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


    fun sendEmail(map: HashMap<String, Any>) {

        apiService.sendEmail(map).enqueue(object : Callback<CommonResponse> {

            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {

                if (response.isSuccessful) {

                    emailApiData.postValue(response.body()?.message)
                } else {

                    if (response.body()?.message != null)
                    shareEmailError.postValue(response.message())
                }

            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {

                shareEmailError.postValue(t.message)
            }
        })
    }

    var freeEventLiveData: MutableLiveData<PaymentResponse> = MutableLiveData()

    fun subscribeToFreeEvent(hashMap: HashMap<String, Any>) {

        apiService.subscribeEvent(hashMap)
            .enqueue(object : Callback<PaymentResponse> {

                override fun onResponse(
                    call: Call<PaymentResponse>,
                    response: Response<PaymentResponse>
                ) {

                    if (response.isSuccessful) {

                        freeEventLiveData.postValue(response.body())

                    } else {

                        if (response.body()?.message != null)
                            errorToast.postValue(response.body()?.message)

                    }

                }

                override fun onFailure(call: Call<PaymentResponse>, t: Throwable) {

                    errorToast.postValue(t.message)
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
