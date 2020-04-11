package `in`.bitspilani.eon.event_organiser.ui

import `in`.bitspilani.eon.R
import `in`.bitspilani.eon.event_organiser.viewmodel.AddInviteeViewModel
import `in`.bitspilani.eon.event_organiser.viewmodel.EventDetailOrganiserViewModel
import `in`.bitspilani.eon.utils.clickWithDebounce
import `in`.bitspilani.eon.utils.getViewModelFactory
import android.os.Bundle
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


/**
 * A simple [Fragment] subclass.
 *
 */
class AddInviteeFragment(private val callbackListener: CallbackListener) : DialogFragment() {

    private val addInviteeViewModel by viewModels<AddInviteeViewModel> { getViewModelFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isCancelable = false
        return inflater.inflate(R.layout.fragment_add_invitee, container, false)
    }
    override fun getTheme(): Int {
        return R.style.DialogTheme
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addInviteeViewModel.adInvitee(3,25, listOf("ashu@gmail.com","aa@gmail.com"))

        setUpClickListeners()
        setObservables()
    }

    private fun setObservables() {

        addInviteeViewModel.addInviteeLiveData.observe(viewLifecycleOwner, Observer {

            dismiss()
            Toast.makeText(activity, "Invitees added successfully.", Toast.LENGTH_LONG).show()
        })

    }

    private fun setUpClickListeners() {
        btn_close.clickWithDebounce { dismiss() }
        btn_invitee_cancel.clickWithDebounce {    dismiss() }
        btn_invitee_confirm.clickWithDebounce {
            if(!TextUtils.isEmpty(edt_updated_fees.text) && !TextUtils.isEmpty(edt_email_addresses.text)
                && !TextUtils.isEmpty(edt_discount.text))
            {

                addInviteeViewModel.adInvitee(3,25, listOf("ashu@gmail.com","aa@gmail.com"))

            }else{

                Toast.makeText(activity, "Please enter valid details", Toast.LENGTH_LONG).show()

            }
        }

    }


}

interface CallbackListener {
    fun onDataReceived(data: String)
}
