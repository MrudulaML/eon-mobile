package `in`.bitspilani.eon.event_organiser.ui.adapter

import `in`.bitspilani.eon.databinding.EventItemRowBinding
import `in`.bitspilani.eon.event_organiser.models.MonoEvent
import `in`.bitspilani.eon.event_organiser.viewmodel.EventDashboardViewModel
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList


class EventAdapter(
    private val eventList: ArrayList<MonoEvent>,
    val eventDashboardViewModel: EventDashboardViewModel,val isSubscriber: Boolean
) : RecyclerView.Adapter<EventAdapter.EventViewHolder>(), Filterable {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = EventItemRowBinding.inflate(inflater,parent,false)
        return EventViewHolder(binding)
    }

    var eventFilteredList = ArrayList<MonoEvent>()

    init {
        eventFilteredList =eventList
    }

    override fun getItemCount(): Int = eventFilteredList.size

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) =
        holder.bind(eventFilteredList[position])

    inner class EventViewHolder(private val binding: EventItemRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MonoEvent) {

            binding.event = item
            binding.isSubscriber= isSubscriber
            binding.dashboardViewmodel = eventDashboardViewModel
            binding.executePendingBindings()
        }


    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                Timber.e("event filter $charSearch")
               if (charSearch.isEmpty()) {
                   eventFilteredList = eventList
                } else {
                    val resultList = ArrayList<MonoEvent>()
                    for (item in eventList) {
                        if (item.name.toLowerCase(Locale.ROOT).contains(charSearch.toLowerCase(Locale.ROOT))
                            ||item.location.toLowerCase(Locale.ROOT).contains(charSearch.toLowerCase(Locale.ROOT))) {
                            resultList.add(item)
                        }
                    }
                    eventFilteredList= resultList
                }
                val filterResults = FilterResults()
                filterResults.values = eventFilteredList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

                eventFilteredList = results?.values as ArrayList<MonoEvent>
                notifyDataSetChanged()
            }

        }
    }

}
