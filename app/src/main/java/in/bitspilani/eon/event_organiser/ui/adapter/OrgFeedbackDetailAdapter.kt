package `in`.bitspilani.eon.event_organiser.ui.adapter

import `in`.bitspilani.eon.databinding.RowOrgFeedbackDetailBinding
import `in`.bitspilani.eon.databinding.RowOrganiserFeedbackBinding
import `in`.bitspilani.eon.event_organiser.models.FeedbackUser
import `in`.bitspilani.eon.event_organiser.models.Responses
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class OrgFeedbackDetailAdapter(var userList: ArrayList<`in`.bitspilani.eon.event_organiser.models.Responses>) :
    RecyclerView.Adapter<OrgFeedbackDetailAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val binding = RowOrgFeedbackDetailBinding.inflate(inflater)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {

        return userList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(userList[position], position)


    inner class ViewHolder(val binding: RowOrgFeedbackDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Responses, position: Int) {
            with(binding) {

                binding.responseData = item
                binding.executePendingBindings()


            }
        }
    }
}