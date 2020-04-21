package `in`.bitspilani.eon.event_subscriber.subscriber.feedback

import `in`.bitspilani.eon.databinding.RowFeedbackBinding
import `in`.bitspilani.eon.event_subscriber.models.Feedback
import `in`.bitspilani.eon.utils.clickWithDebounce
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.payment_fragment.*

class FeedbackAdapter(var feedbackList: ArrayList<Feedback>, val onAttachmentClick: (position: Int) -> Unit) : RecyclerView.Adapter<FeedbackAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val binding = RowFeedbackBinding.inflate(inflater)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {

        return feedbackList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(feedbackList[position], position)

    inner class ViewHolder(val binding: RowFeedbackBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Feedback, position: Int) {
            with(binding) {

                item.questionNumber = position + 1
                binding.feedbackData = item
                binding.llActionAttachment.clickWithDebounce {

                    onAttachmentClick.invoke(position)
                }

                binding.etAnswer.addTextChangedListener(object : TextWatcher {

                    override fun afterTextChanged(s: Editable) {



                    }

                    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

                    }

                    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                        feedbackList[position].answer= binding.etAnswer.text.toString()

                    }
                })
                binding.executePendingBindings()
            }
        }
    }
}