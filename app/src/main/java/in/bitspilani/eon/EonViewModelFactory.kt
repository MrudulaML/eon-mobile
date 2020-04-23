package `in`.bitspilani.eon

import `in`.bitspilani.eon.analytics.AnalyticsViewModel
import `in`.bitspilani.eon.api.ApiService
import `in`.bitspilani.eon.event_organiser.viewmodel.*
import `in`.bitspilani.eon.event_subscriber.subscriber.detail.EventDetailsViewModel
import `in`.bitspilani.eon.event_subscriber.subscriber.feedback.FeedbackViewmodel
import `in`.bitspilani.eon.event_subscriber.subscriber.payments.PaymentViewModel
import `in`.bitspilani.eon.event_subscriber.subscriber.summary.EventSummaryViewModel
import `in`.bitspilani.eon.login.ui.ChangePwViewModel
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
            //only Put non shared View Models here
            isAssignableFrom(ChangePwViewModel::class.java) -> ChangePwViewModel(apiService)
            isAssignableFrom(EventDetailsViewModel::class.java) -> EventDetailsViewModel(apiService)
            isAssignableFrom(EventFilterViewModel::class.java) -> EventFilterViewModel(apiService)
            isAssignableFrom(EventDetailOrganiserViewModel::class.java) -> EventDetailOrganiserViewModel(apiService)
            isAssignableFrom(AddInviteeViewModel::class.java) -> AddInviteeViewModel(apiService)
            isAssignableFrom(NotificationViewModel::class.java) -> NotificationViewModel(apiService)
            isAssignableFrom(PaymentViewModel::class.java) -> PaymentViewModel(apiService)
            isAssignableFrom(EventSummaryViewModel::class.java) -> EventSummaryViewModel(apiService)
            isAssignableFrom(UserProfileViewModel::class.java) -> UserProfileViewModel(apiService)
            isAssignableFrom(FeedbackViewmodel::class.java) -> FeedbackViewmodel(apiService)
            isAssignableFrom(OrgFeedbackViewmodel::class.java) -> OrgFeedbackViewmodel(apiService)
            isAssignableFrom(AnalyticsViewModel::class.java) -> AnalyticsViewModel(apiService)

            else ->
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    } as T

}

