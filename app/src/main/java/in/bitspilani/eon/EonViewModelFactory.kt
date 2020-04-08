package `in`.bitspilani.eon
import `in`.bitspilani.eon.api.ApiService
import `in`.bitspilani.eon.eventOrganiser.ui.EventDashboardViewModel
import `in`.bitspilani.eon.login.ui.AuthViewModel
import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner


@Suppress("UNCHECKED_CAST")
class EonViewModelFactory(
    owner: SavedStateRegistryOwner,
    private val apiService: ApiService,
    defaultArgs: Bundle? = null
) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {
    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ) = with(modelClass) {
        when {
            isAssignableFrom(AuthViewModel::class.java) -> AuthViewModel()
            isAssignableFrom(EventDashboardViewModel::class.java) -> EventDashboardViewModel(apiService)
            else ->
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    } as T

}

