package `in`.bitspilani.eon.utils

import `in`.bitspilani.eon.BuildConfig
import `in`.bitspilani.eon.data.remote.ErrorResponse
import `in`.bitspilani.eon.data.remote.RestClient
import android.text.TextUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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
                if (TextUtils.isEmpty(errorMessage))
                    errorMessage = errors.detail
                if (response.code()<500){
                    onApiError(ApiError.REQUEST_ERROR,errorMessage)
                }else{
                    onApiError(ApiError.SERVER_ERROR,errorMessage)
                }
            } catch (e: Exception) {
                onApiError(ApiError.SERVER_ERROR,null)

            }

        }
    }
    override fun onFailure(call: Call<T>, t: Throwable) {
        if(BuildConfig.DEBUG) {
            t.printStackTrace()
        }
        if(t is SocketTimeoutException || t is SocketException){
            onApiError(ApiError.TIMEOUT_ERROR,null)
        }else if(t is UnknownHostException){
            onApiError(ApiError.NETWORK_ERROR,null)
        }
    }

    enum class ApiError {
        REQUEST_ERROR,
        SERVER_ERROR,
        TIMEOUT_ERROR,
        NETWORK_ERROR
    }
}
