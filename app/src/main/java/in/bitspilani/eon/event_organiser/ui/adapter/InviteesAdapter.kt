package `in`.bitspilani.eon.event_organiser.ui.adapter

import `in`.bitspilani.eon.databinding.InviteeItemRowBinding
import `in`.bitspilani.eon.event_organiser.models.Invitee
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.invitee_item_row.view.*
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList


class InviteesAdapter(private val inviteeList: ArrayList<Invitee>, var deleteItemCallback: (position:Invitee) -> Unit) :
    RecyclerView.Adapter<InviteesAdapter.InviteesViewHolder>(),
    Filterable {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InviteesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = InviteeItemRowBinding.inflate(inflater,parent,false)
        return InviteesViewHolder(binding)
    }

    var inviteeFilteredList = ArrayList<Invitee>()

    init {
        inviteeFilteredList = inviteeList
    }
    override fun getItemCount(): Int = inviteeList.size

    override fun onBindViewHolder(holder: InviteesViewHolder, position: Int) {
        holder.bind(inviteeList[position])
    }

    fun removeAt(position: Invitee) {
        inviteeFilteredList.remove(position)
        notifyDataSetChanged()
    }

    inner class InviteesViewHolder(private val binding: InviteeItemRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Invitee) {
            binding.invitee = item

            itemView.chb_invitee.setOnCheckedChangeListener { buttonView, isChecked ->


            }
           /* itemView.button_delete.setOnClickListener {

                if(itemView.chb_invitee.isChecked){
                    deleteItemCallback(item)
                    itemView.chb_invitee.isChecked=false
                }
            }*/
            binding.executePendingBindings()
        }

    }



    fun filter(query: String) {

        if (query.isBlank()) {
            Timber.e("filter is empty")

            inviteeFilteredList= inviteeList

            Timber.e("filter is empty${inviteeList.size}")

        } else {
            val result: ArrayList<Invitee> = ArrayList()
            for (item in inviteeFilteredList) {
                if (item.email.toLowerCase(Locale.ROOT).contains(query.toLowerCase(Locale.ROOT))) {
                    result.add(item)
                }
            }
            inviteeFilteredList.clear()
            inviteeFilteredList.addAll(result)
        }
        notifyDataSetChanged()
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
