package `in`.bitspilani.eon.event_organiser.ui.adapter

import `in`.bitspilani.eon.databinding.InviteeItemRowBinding
import `in`.bitspilani.eon.event_organiser.models.Invitee
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


class InviteesAdapter(private val evenList : List<Invitee>): RecyclerView.Adapter<InviteesAdapter.InviteesViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InviteesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = InviteeItemRowBinding.inflate(inflater)
        return InviteesViewHolder(binding)
    }

    override fun getItemCount(): Int = evenList.size

    override fun onBindViewHolder(holder: InviteesViewHolder, position: Int) = holder.bind(evenList[position])

    inner class InviteesViewHolder(private val binding: InviteeItemRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Invitee) {

            binding.invitee = item
            binding.executePendingBindings()




        }


    }

}
