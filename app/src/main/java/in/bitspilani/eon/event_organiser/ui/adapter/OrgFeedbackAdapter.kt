package `in`.bitspilani.eon.event_organiser.ui.adapter

import `in`.bitspilani.eon.databinding.RowFeedbackBinding
import `in`.bitspilani.eon.databinding.RowOrganiserFeedbackBinding
import `in`.bitspilani.eon.event_subscriber.models.Feedback
import `in`.bitspilani.eon.utils.clickWithDebounce
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class OrgFeedbackAdapter  : RecyclerView.Adapter<OrgFeedbackAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val binding = RowOrganiserFeedbackBinding.inflate(inflater)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {

        return 10
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }

    inner class ViewHolder(val binding: RowOrganiserFeedbackBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Feedback, position: Int) {
            with(binding) {


            }
        }
    }
    }