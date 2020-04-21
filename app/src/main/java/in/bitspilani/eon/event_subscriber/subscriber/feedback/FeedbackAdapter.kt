package `in`.bitspilani.eon.event_subscriber.subscriber.feedback

import `in`.bitspilani.eon.databinding.RowFeedbackBinding
import `in`.bitspilani.eon.event_subscriber.models.Feedback
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class FeedbackAdapter(private val items: ArrayList<Feedback>) : RecyclerView.Adapter<FeedbackAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val binding = RowFeedbackBinding.inflate(inflater)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {

        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])

    inner class ViewHolder(val binding: RowFeedbackBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Feedback) {
            with(binding) {

                binding.feedbackData = item
                binding.executePendingBindings()
            }
        }
    }
}