package `in`.bitspilani.eon.event_organiser.ui

import `in`.bitspilani.eon.BitsEonActivity
import `in`.bitspilani.eon.R
import `in`.bitspilani.eon.event_organiser.ui.adapter.OrgFeedbackAdapter
import `in`.bitspilani.eon.event_organiser.viewmodel.OrgFeedbackViewmodel
import `in`.bitspilani.eon.event_subscriber.subscriber.feedback.FeedbackAdapter
import `in`.bitspilani.eon.event_subscriber.subscriber.feedback.FeedbackViewmodel
import `in`.bitspilani.eon.event_subscriber.subscriber.payments.PaymentViewModel
import `in`.bitspilani.eon.login.data.Data
import `in`.bitspilani.eon.login.ui.ActionbarHost
import `in`.bitspilani.eon.utils.Constants
import `in`.bitspilani.eon.utils.ModelPreferencesManager
import `in`.bitspilani.eon.utils.clickWithDebounce
import `in`.bitspilani.eon.utils.getViewModelFactory
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_feedback.*
import kotlinx.android.synthetic.main.fragment_organiser_feedback.*
import kotlinx.android.synthetic.main.layout_dialog_payment_success.view.*
import kotlinx.android.synthetic.main.payment_fragment.*

class OrganiserFeedbackFragment : Fragment() {

    private val orgFeedbackViewmodel by viewModels<OrgFeedbackViewmodel> { getViewModelFactory() }

    private var actionbarHost: ActionbarHost? = null

    var eventId: Int = 0

    var hashMap: HashMap<String, Any> = HashMap()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_organiser_feedback, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        actionbarHost?.showToolbar(showToolbar = false, showBottomNav = false)

        setObservables()
        init()
    }


    fun init() {

        orgFeedbackViewmodel.getUsers(arguments!!.getInt(Constants.EVENT_ID, 0))

    }

    fun setObservables() {


        //loader observable
        orgFeedbackViewmodel.progressLiveData.observe(viewLifecycleOwner, Observer {

            (activity as BitsEonActivity).showProgress(it)
        })

        orgFeedbackViewmodel.feedbackListData.observe(viewLifecycleOwner, Observer {

            rv_org_feedback.layoutManager = LinearLayoutManager(activity!!)
            rv_org_feedback.adapter = OrgFeedbackAdapter(it.data)
        })
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
