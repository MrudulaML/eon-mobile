package `in`.bitspilani.eon.event_organiser.ui

import `in`.bitspilani.eon.R
import `in`.bitspilani.eon.event_organiser.viewmodel.AddInviteeViewModel
import `in`.bitspilani.eon.event_organiser.viewmodel.EventDetailOrganiserViewModel
import `in`.bitspilani.eon.utils.clickWithDebounce
import `in`.bitspilani.eon.utils.getViewModelFactory
import `in`.bitspilani.eon.utils.hideKeyboard
import android.os.Bundle
import android.os.Message
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_add_invitee.*
import kotlinx.android.synthetic.main.fragment_add_invitee.btn_close
import kotlinx.android.synthetic.main.fragment_notify_subscriber.*


/**
 * A simple [Fragment] subclass.
 *
 */
class NotifySubscriberFragment(var notifySubscriberCallback: NotifySubscriberCallback) : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isCancelable = false
        return inflater.inflate(R.layout.fragment_notify_subscriber, container, false)
    }
    override fun getTheme(): Int {
        return R.style.DialogTheme
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setUpClickListeners()
        setObservables()
    }

    private fun setObservables() {


    }

    private fun setUpClickListeners() {
        btn_close.clickWithDebounce { dismiss() }
        btn_notify_confirm.clickWithDebounce {
            edt_notify_message.text?.let {
                notifySubscriberCallback.onUpdate(edt_notify_message.text.toString())
            }
            hideKeyboard(activity)
            dismiss()
        }

    }


}

interface NotifySubscriberCallback {
    fun onUpdate(message:String)
    fun onReminder(message:String)
}

