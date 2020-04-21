package `in`.bitspilani.eon.analytics

import `in`.bitspilani.eon.analytics.data.OrganizedEvent
import `in`.bitspilani.eon.databinding.AnalyticsItemRowBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList


class OrganizedEventAdapter(private val eventList: ArrayList<OrganizedEvent>) :
    RecyclerView.Adapter<OrganizedEventAdapter.EventViewHolder>(),
    Filterable {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AnalyticsItemRowBinding.inflate(inflater,parent,false)
        return EventViewHolder(binding)
    }

    var selectAll:Boolean =false
    var eventFilteredList = ArrayList<OrganizedEvent>()

    init {
        eventFilteredList = eventList
    }
    override fun getItemCount(): Int = eventFilteredList.size

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(eventFilteredList[position])
    }

    inner class EventViewHolder(private val binding: AnalyticsItemRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: OrganizedEvent) {

            binding.organizedEvent = item
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
                    val resultList = ArrayList<OrganizedEvent>()
                    for (item in eventList) {
                        if (item.name.toLowerCase(Locale.ROOT).contains(charSearch.toLowerCase(Locale.ROOT))) {
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

                eventFilteredList = results?.values as ArrayList<OrganizedEvent>
                notifyDataSetChanged()
            }

        }
    }

}
