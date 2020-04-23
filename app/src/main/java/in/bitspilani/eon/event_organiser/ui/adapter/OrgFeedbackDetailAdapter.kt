package `in`.bitspilani.eon.event_organiser.ui.adapter

import `in`.bitspilani.eon.R
import `in`.bitspilani.eon.event_organiser.models.FeedbackUser
import `in`.bitspilani.eon.event_organiser.models.Responses
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.row_org_feedback_detail.view.*
import java.security.cert.Extension

class OrgFeedbackDetailAdapter(val context: Context, val arr: ArrayList<Responses>) :
    RecyclerView.Adapter<PageViewHolder>() {

    override fun getItemCount(): Int {
        return arr.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageViewHolder {
        var layoutInflater: LayoutInflater = LayoutInflater.from(context)


        return PageViewHolder(layoutInflater.inflate(R.layout.row_org_feedback_detail, parent, false)
        )
    }

    override fun onBindViewHolder(holder: PageViewHolder, position: Int) {
        holder.bind(arr.get(position), position)
    }
}

class PageViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
    fun bind(item: Responses, position: Int) {


        view.tv_answer.text = item.answer
        view.tv_question.text = item.question
        view.tv_question_number.text = (position + 1).toString()

        var imageUrl: String? = item.image

        if (imageUrl != null && !imageUrl.isEmpty())
            Picasso.get().load(imageUrl).into(view.iv_attached_image)
        else {

            view.iv_attached_image.visibility=View.GONE

        }
    }
}