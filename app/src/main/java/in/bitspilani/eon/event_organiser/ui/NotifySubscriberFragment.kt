package `in`.bitspilani.eon.event_organiser.ui

import `in`.bitspilani.eon.R
import `in`.bitspilani.eon.utils.clickWithDebounce
import `in`.bitspilani.eon.utils.hideKeyboard
import `in`.bitspilani.eon.utils.showSnackbar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_add_invitee.btn_close
import kotlinx.android.synthetic.main.fragment_notify_subscriber.*


/**
 * A simple [Fragment] subclass.
 *
 */
class NotifySubscriberFragment(
    var notifySubscriberCallback: NotifySubscriberCallback,
    private val fromUpdate: Boolean
) : DialogFragment() {

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

        if(fromUpdate)
            notify_dialog_title.text="Send Updates"
        else
            notify_dialog_title.text="Send Reminder"
        setUpClickListeners()
        setObservables()
    }

    private fun setObservables() {


    }

    private fun setUpClickListeners() {
        btn_close.clickWithDebounce { dismiss() }
        btn_notify_confirm.clickWithDebounce {
            if(!edt_notify_message.text.isNullOrEmpty())
            {
                if(fromUpdate)
                    notifySubscriberCallback.onUpdate(edt_notify_message.text.toString())
                else
                    notifySubscriberCallback.onReminder(edt_notify_message.text.toString())
            }else{

                view?.showSnackbar("Message can not be empty",0)
            }

            dismiss()
        }

    }


}

interface NotifySubscriberCallback {
    fun onUpdate(message:String)
    fun onReminder(message:String)
}

