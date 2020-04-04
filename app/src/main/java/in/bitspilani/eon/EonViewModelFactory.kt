package `in`.bitspilani.eon
import `in`.bitspilani.eon.api.RestClient
import `in`.bitspilani.eon.event.data.EventRepository
import `in`.bitspilani.eon.event.ui.HomeViewModel
import `in`.bitspilani.eon.login.ui.AuthViewModel
import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner


@Suppress("UNCHECKED_CAST")
class EonViewModelFactory(
    owner: SavedStateRegistryOwner,
    private val restClient : RestClient,
    defaultArgs: Bundle? = null
) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {
    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ) = with(modelClass) {
        when {
            isAssignableFrom(AuthViewModel::class.java) -> AuthViewModel()
            isAssignableFrom(HomeViewModel::class.java) -> createHomeViewModel(restClient)
            else ->
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    } as T

    private fun createHomeViewModel(restClient: RestClient): HomeViewModel {
        val eventRepository =EventRepository(restClient)
        return HomeViewModel(eventRepository)

    }


}

