package `in`.bitspilani.eon.api

import `in`.bitspilani.eon.BuildConfig
import `in`.bitspilani.eon.utils.Constants
import `in`.bitspilani.eon.utils.ModelPreferencesManager
import com.facebook.stetho.okhttp3.StethoInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

class RestClient{

    @Singleton
    val authClient: Retrofit
        get() {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .followRedirects(true)
                .followSslRedirects(true)
                .retryOnConnectionFailure(true)
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .cache(null)
                .addInterceptor(AuthInterceptor())
                .addNetworkInterceptor(StethoInterceptor())
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
            return retrofit!!

        }

    @Singleton
    val noAuthClient: Retrofit
        get() {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .followRedirects(true)
                .followSslRedirects(true)
                .retryOnConnectionFailure(true)
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .addNetworkInterceptor(StethoInterceptor())
                .cache(null)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
            return retrofit!!

        }


}

class AuthInterceptor: Interceptor {

    /**
     * Interceptor class for setting of the headers for every request
     */
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = ModelPreferencesManager.getString(Constants.ACCESS_TOKEN)
        val original = chain.request()
        val request = original.newBuilder()
            .header("Authorization", "Bearer $token")
            .header("Content-Type","application/json")
            .method(original.method(), original.body())
            .build()
        return chain.proceed(request)
    }
}
