package `in`.bitspilani.eon.eventOrganiser.ui.adapter

import `in`.bitspilani.eon.databinding.EventItemRowBinding
import `in`.bitspilani.eon.eventOrganiser.data.MonoEvent
import `in`.bitspilani.eon.eventOrganiser.viewmodel.EventDetailsViewModel
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView


class EventAdapter(private val eventList : List<MonoEvent>,
                   val eventDashboardViewModel: EventDetailsViewModel
) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = EventItemRowBinding.inflate(inflater)
        return EventViewHolder(binding)
    }

    override fun getItemCount(): Int = eventList.size

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) = holder.bind(eventList[position])

    inner class EventViewHolder(private val binding: EventItemRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MonoEvent) {

            binding.event = item
            binding.dashboardViewmodel=eventDashboardViewModel
            binding.executePendingBindings()
        }


    }

}
