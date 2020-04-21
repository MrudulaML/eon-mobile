package `in`.bitspilani.eon.event_subscriber.subscriber.feedback

import `in`.bitspilani.eon.R
import `in`.bitspilani.eon.event_subscriber.models.Feedback
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
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_feedback.*
import kotlinx.android.synthetic.main.layout_dialog_payment_success.view.*
import kotlinx.android.synthetic.main.payment_fragment.*

class FeedbackFragment : Fragment() {

    private val feedbackViewmodel by viewModels<FeedbackViewmodel> { getViewModelFactory() }
    private var actionbarHost: ActionbarHost? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_feedback, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        actionbarHost?.showToolbar(
            showToolbar = true,
            title = "Send Feedback",
            showBottomNav = false
        )

        setRecyclerview()

    }

    fun setRecyclerview() {

        var list: ArrayList<Feedback> = ArrayList<Feedback>()

        list.add(Feedback("Are you mad at me?"))
        list.add(Feedback("Lets play holi?"))
        list.add(Feedback("Did you like our event?"))
        list.add(Feedback("Do you like eminem"))

        rv_subscriber_feedback.layoutManager = LinearLayoutManager(activity)
        rv_subscriber_feedback.adapter = FeedbackAdapter(list)


    }

    fun setObservables() {


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