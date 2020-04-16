package `in`.bitspilani.eon.event_organiser.ui

import `in`.bitspilani.eon.R
import `in`.bitspilani.eon.event_organiser.models.Data
import `in`.bitspilani.eon.event_organiser.models.Invitee
import `in`.bitspilani.eon.event_organiser.viewmodel.AddInviteeViewModel
import `in`.bitspilani.eon.utils.clickWithDebounce
import `in`.bitspilani.eon.utils.getViewModelFactory
import `in`.bitspilani.eon.utils.showSnackbar
import android.app.AlertDialog
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
import com.google.gson.JsonArray
import com.hootsuite.nachos.terminator.ChipTerminatorHandler
import kotlinx.android.synthetic.main.dialog_email_update.view.*
import kotlinx.android.synthetic.main.fragment_add_invitee.*
import kotlinx.android.synthetic.main.layout_dialog_payment_success.view.*
import timber.log.Timber


/**
 * A simple [Fragment] subclass.
 *
 */
class AddInviteeFragment(private val eventData: Data, private val callbackListener: CallbackListener) : DialogFragment() {

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

        text_updated_fees.text= eventData.subscription_fee.toString()
        setUpClickListeners()
        setObservables()
    }

    private fun setObservables() {

        addInviteeViewModel.addInviteeLiveData.observe(viewLifecycleOwner, Observer {

            dismiss()

            callbackListener.onDataReceived(it.data.invitee_list)
            //Toast.makeText(activity, "Invitees added successfully.", Toast.LENGTH_LONG).show()
        })

        nacho_text_view.addChipTerminator(',', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_ALL);


    }

    private fun setUpClickListeners() {
        btn_close.clickWithDebounce { dismiss() }
        btn_invitee_cancel.clickWithDebounce { dismiss() }
        btn_invitee_confirm.clickWithDebounce {
            if (!TextUtils.isEmpty(edt_discount.text))
            {
                val listOfEmail = ArrayList<String>()
                //get emails from chips
                for (chip in nacho_text_view.allChips) {

                    listOfEmail.add(chip.text.toString())

                }
                val array = JsonArray()
                for (each in listOfEmail) {
                    array.add(each)
                }
                Timber.e("invitee_list$array")
               addInviteeViewModel.adInvitee(eventData.id, edt_discount.text.toString().toInt(), array)

            } else {

                Toast.makeText(activity, "Please enter valid details", Toast.LENGTH_LONG).show()

            }
        }


    }




}

interface CallbackListener {
    fun onDataReceived(inviteeList: ArrayList<Invitee>)
}
