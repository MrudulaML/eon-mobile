package `in`.bitspilani.eon.event.ui



import `in`.bitspilani.eon.api.ApiService
import `in`.bitspilani.eon.event.data.IndividualEvent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class EventDashboardViewModel(private val apiService: ApiService): ViewModel() {

    private val mutableLiveData: MutableLiveData<List<IndividualEvent>> =
        MutableLiveData<List<IndividualEvent>>()

    fun observeEventResponse() {

        CoroutineScope(Dispatchers.IO).launch {
            val response = apiService.getEvents()
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