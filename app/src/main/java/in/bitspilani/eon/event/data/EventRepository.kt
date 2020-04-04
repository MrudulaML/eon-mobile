package `in`.bitspilani.eon.event.data

import `in`.bitspilani.eon.api.AuthService
import `in`.bitspilani.eon.api.RestClient
import `in`.bitspilani.eon.utils.ApiCallback
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class EventRepository(private val restClient: RestClient) {

    private val mutableLiveData: MutableLiveData<List<IndividualEvent>> =
        MutableLiveData<List<IndividualEvent>>()

     fun observeEventResponse() {

         CoroutineScope(Dispatchers.IO).launch {
             val response = restClient.authClient.create(AuthService::class.java).getEvents()
             withContext(Dispatchers.Main) {
                 try {
                     if (response.isSuccessful) {
                         //Do something with response e.g show to the UI.
                     } else {
                         //toast("Error: ${response.code()}")
                     }
                 } catch (e: HttpException) {
                     //toast("Exception ${e.message}")
                 } catch (e: Throwable) {
                     //toast("Ooops: Something else went wrong")
                 }
             }
         }

    }
}