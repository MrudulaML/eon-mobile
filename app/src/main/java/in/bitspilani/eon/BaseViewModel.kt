package `in`.bitspilani.eon

import `in`.bitspilani.eon.utils.SingleLiveEvent
import androidx.lifecycle.ViewModel

open class BaseViewModel : ViewModel() {
    val progressLiveData: SingleLiveEvent<Boolean> = SingleLiveEvent()

    fun showProgress(show: Boolean) {
        if (show)
            progressLiveData.postValue(true)
        else
            progressLiveData.postValue(false)
    }
}