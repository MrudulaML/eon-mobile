package `in`.bitspilani.eon.presentation.event.adapter

import `in`.bitspilani.eon.data.restservice.models.IndividualEvent
import `in`.bitspilani.eon.databinding.EventItemRowBinding
import `in`.bitspilani.eon.viewmodel.HomeViewModel
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class EventAdapter(private val evenList : List<IndividualEvent>,val homeViewModel: HomeViewModel) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = EventItemRowBinding.inflate(inflater)
        return EventViewHolder(binding)
    }

    override fun getItemCount(): Int = evenList.size

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) = holder.bind(evenList[position])

    inner class EventViewHolder(private val binding: EventItemRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: IndividualEvent) {

            binding.event = item
            binding.homeViewModel=homeViewModel
            binding.executePendingBindings()
        }


    }

}
