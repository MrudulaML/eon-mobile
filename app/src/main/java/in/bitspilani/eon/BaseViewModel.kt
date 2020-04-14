package `in`.bitspilani.eon

import `in`.bitspilani.eon.utils.SingleLiveEvent
import androidx.lifecycle.ViewModel

open class BaseViewModel : ViewModel() {
    val progressLiveData: SingleLiveEvent<Boolean> = SingleLiveEvent()
    val errorView: SingleLiveEvent<String> = SingleLiveEvent()

    fun showProgress(show: Boolean) {
        if (show)
            progressLiveData.postValue(true)
        else
            progressLiveData.postValue(false)
    }
}