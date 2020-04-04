package `in`.bitspilani.eon.event.ui



import `in`.bitspilani.eon.event.data.EventRepository
import androidx.lifecycle.ViewModel

class HomeViewModel(private val eventRepository: EventRepository): ViewModel() {

    val eventList by lazy { eventRepository.observeEventResponse()}

}