package `in`.bitspilani.eon.event_organiser.ui.adapter

import `in`.bitspilani.eon.databinding.RowFeedbackBinding
import `in`.bitspilani.eon.databinding.RowOrganiserFeedbackBinding
import `in`.bitspilani.eon.event_organiser.models.FeedbackUser
import `in`.bitspilani.eon.event_organiser.viewmodel.OrgFeedbackViewmodel
import `in`.bitspilani.eon.event_subscriber.models.FeedbackData
import `in`.bitspilani.eon.utils.clickWithDebounce
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class OrgFeedbackAdapter(var userList: ArrayList<`in`.bitspilani.eon.event_organiser.models.FeedbackData>,var orgFeedbackViewmodel: OrgFeedbackViewmodel)  : RecyclerView.Adapter<OrgFeedbackAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val binding = RowOrganiserFeedbackBinding.inflate(inflater)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {

        return userList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(userList[position].user,position)



    inner class ViewHolder(val binding: RowOrganiserFeedbackBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: FeedbackUser, position: Int) {
            with(binding) {

                binding.feedbackUserData=item
                binding.llRoot.clickWithDebounce {

                    orgFeedbackViewmodel.onSingleUserFeedbackClick(userList[position])
                }
                binding.executePendingBindings()


            }
        }
    }
    }