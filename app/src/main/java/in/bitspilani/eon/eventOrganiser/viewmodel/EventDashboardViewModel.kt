package `in`.bitspilani.eon.eventOrganiser.viewmodel



import `in`.bitspilani.eon.api.ApiService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class EventDashboardViewModel(private val apiService: ApiService): ViewModel() {

    /* val mutableLiveData: MutableLiveData<List<IndividualEvent>> =
        MutableLiveData<List<IndividualEvent>>()*/



    /*fun observeEventResponse() {

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

    }*/


}