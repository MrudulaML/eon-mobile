package `in`.bitspilani.eon.utils

import `in`.bitspilani.eon.BuildConfig
import `in`.bitspilani.eon.api.ErrorResponse
import `in`.bitspilani.eon.api.RestClient
import android.text.TextUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException


abstract class ApiCallback<T> : Callback<T> {


    abstract fun onSuccessResponse(responseBody:T)

    abstract fun onApiError(errorType:ApiError, error:String?)



    override fun onResponse(call: Call<T>, response: Response<T>) {
        if (response.isSuccessful&&response.body()!=null){
            onSuccessResponse(response.body()!!)
        }else{
            val converter = RestClient().authClient.responseBodyConverter<ErrorResponse>(
                ErrorResponse::class.java, arrayOfNulls(0))
            try {
                val errors = converter.convert(response.errorBody())
                var errorMessage = errors!!.message
                if (TextUtils.isEmpty(errorMessage)){
                    errorMessage = errors.message
                    Timber.e("Error$errorMessage")
                }
                if (response.code()<500){
                    onApiError(ApiError.REQUEST_ERROR,errorMessage)
                }else{
                    onApiError(ApiError.SERVER_ERROR,errorMessage)
                }
            } catch (e: Exception) {
                onApiError(ApiError.SERVER_ERROR,"Something went wrong")

            }

        }
    }

    override fun onFailure(call: Call<T>, t: Throwable) {
        if(BuildConfig.DEBUG) {
            t.printStackTrace()
        }
        if(t is SocketTimeoutException || t is SocketException){
            onApiError(ApiError.TIMEOUT_ERROR,"Connection timed out")
        }else if(t is UnknownHostException){
            onApiError(ApiError.NETWORK_ERROR,"Please check your network connection.")
        }
    }

    enum class ApiError {
        REQUEST_ERROR,
        SERVER_ERROR,
        TIMEOUT_ERROR,
        NETWORK_ERROR
    }
}
