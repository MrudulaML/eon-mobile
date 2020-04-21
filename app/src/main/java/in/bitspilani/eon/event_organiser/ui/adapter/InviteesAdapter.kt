package `in`.bitspilani.eon.event_organiser.ui.adapter

import `in`.bitspilani.eon.databinding.InviteeItemRowBinding
import `in`.bitspilani.eon.event_organiser.models.Invitee
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.invitee_item_row.view.*
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList


class InviteesAdapter(private val inviteeList: ArrayList<Invitee>, var selectCheckBoxCallback: (position:Invitee,isSelected:Boolean) -> Unit) :
    RecyclerView.Adapter<InviteesAdapter.InviteesViewHolder>(),
    Filterable {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InviteesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = InviteeItemRowBinding.inflate(inflater,parent,false)
        return InviteesViewHolder(binding)
    }

    var selectAll:Boolean =false
    var inviteeFilteredList = ArrayList<Invitee>()

    init {
        inviteeFilteredList = inviteeList
    }
    override fun getItemCount(): Int = inviteeFilteredList.size

    override fun onBindViewHolder(holder: InviteesViewHolder, position: Int) {
        holder.bind(inviteeFilteredList[position])
    }

    fun removeAt(position: Invitee) {
        inviteeFilteredList.remove(position)
        notifyDataSetChanged()
    }

    fun selectAll() {
        selectAll = true
        notifyDataSetChanged()
    }

    fun deSelectAll() {
        selectAll = false
        notifyDataSetChanged()
    }
    inner class InviteesViewHolder(private val binding: InviteeItemRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Invitee) {
            item.selectAll=selectAll
            binding.invitee = item

            itemView.chb_invitee.setOnCheckedChangeListener { buttonView, isChecked ->
                selectCheckBoxCallback(item,isChecked)
            }
            binding.executePendingBindings()
        }

    }



    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
               if (charSearch.isEmpty()) {
                   inviteeFilteredList = inviteeList
               } else {
                    val resultList = ArrayList<Invitee>()
                    for (item in inviteeList) {
                        if (item.email.toLowerCase(Locale.ROOT).contains(charSearch.toLowerCase(
                                Locale.ROOT))) {
                            resultList.add(item)
                        }
                    }
                    inviteeFilteredList= resultList
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
