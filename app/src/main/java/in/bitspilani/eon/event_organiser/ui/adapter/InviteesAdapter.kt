package `in`.bitspilani.eon.event_organiser.ui.adapter

import `in`.bitspilani.eon.databinding.InviteeItemRowBinding
import `in`.bitspilani.eon.event_organiser.models.Invitee
import `in`.bitspilani.eon.event_organiser.models.MonoEvent
import android.view.*
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.collections.ArrayList


class InviteesAdapter(private val inviteeList: ArrayList<Invitee>) :
    RecyclerView.Adapter<InviteesAdapter.InviteesViewHolder>(), View.OnCreateContextMenuListener,
    Filterable {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InviteesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = InviteeItemRowBinding.inflate(inflater)
        return InviteesViewHolder(binding)
    }

    var inviteeFilteredList = ArrayList<Invitee>()

    init {
        inviteeFilteredList = inviteeList
    }
    override fun getItemCount(): Int = inviteeList.size

    override fun onBindViewHolder(holder: InviteesViewHolder, position: Int) =
        holder.bind(inviteeList[position])

    inner class InviteesViewHolder(private val binding: InviteeItemRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Invitee) {

            binding.invitee = item
            binding.executePendingBindings()


        }


    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        menu?.setHeaderTitle("Select The Action!");
        menu?.add(0, v!!.id, 0, "City");
        menu?.add(0, v!!.id, 0, "Lga");
    }


    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                inviteeFilteredList = if (charSearch.isEmpty()) {
                    inviteeList
                } else {
                    val resultList = ArrayList<Invitee>()
                    for (item in inviteeFilteredList) {
                        if (item.email.toLowerCase(Locale.ROOT).contains(charSearch.toLowerCase(
                                Locale.ROOT))) {
                            resultList.add(item)
                        }
                    }
                    resultList
                }
                val filterResults = FilterResults()
                filterResults.values = inviteeFilteredList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

                inviteeFilteredList = results?.values as ArrayList<Invitee>
                notifyDataSetChanged()
            }

        }
    }

}
