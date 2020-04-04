package `in`.bitspilani.eon.utils

/**
 * Extension functions for Fragment.
 */

import `in`.bitspilani.eon.EonViewModelFactory
import `in`.bitspilani.eon.api.RestClient
import androidx.fragment.app.Fragment


fun Fragment.getViewModelFactory(): EonViewModelFactory {
    //Add common things here
    val restClient = RestClient()
    return EonViewModelFactory(this,restClient)
}
