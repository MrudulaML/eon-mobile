package `in`.bitspilani.eon.event_organiser.ui

import `in`.bitspilani.eon.BitsEonActivity
import `in`.bitspilani.eon.R
import `in`.bitspilani.eon.event_organiser.models.FeedbackData
import `in`.bitspilani.eon.event_organiser.models.Responses
import `in`.bitspilani.eon.event_organiser.ui.adapter.OrgFeedbackAdapter
import `in`.bitspilani.eon.event_organiser.ui.adapter.OrgFeedbackDetailAdapter
import `in`.bitspilani.eon.event_organiser.viewmodel.OrgFeedbackViewmodel
import `in`.bitspilani.eon.login.ui.ActionbarHost
import `in`.bitspilani.eon.utils.Constants
import `in`.bitspilani.eon.utils.clickWithDebounce
import `in`.bitspilani.eon.utils.getViewModelFactory
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_org_feedback_detail.*
import kotlinx.android.synthetic.main.fragment_organiser_feedback.*

class OrganizerFeedbackDetail : Fragment() {


    private var actionbarHost: ActionbarHost? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_org_feedback_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        actionbarHost?.showToolbar(showToolbar = false, showBottomNav = false)


        init()

    }

    fun init() {

        var gson = Gson()
        var feedbackDataInJsonString = arguments!!.getString(Constants.RESPONSE_LIST)


        var feedbackData: FeedbackData =
            gson.fromJson(feedbackDataInJsonString, FeedbackData::class.java)

        tv_toolbar_text_detail.text = feedbackData.user.email


        rv_org_feedback_detail.layoutManager = LinearLayoutManager(activity!!)
        rv_org_feedback_detail.adapter =
            OrgFeedbackDetailAdapter(activity!!, feedbackData.responses)
        rv_org_feedback_detail.setHasFixedSize(true)


        iv_back_detail.clickWithDebounce {

            findNavController().popBackStack()
        }

    }



    fun showUserMsg(msg: String) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT)
            .show()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ActionbarHost) {
            actionbarHost = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        actionbarHost?.showToolbar(showToolbar = false, showBottomNav = false)
    }
}