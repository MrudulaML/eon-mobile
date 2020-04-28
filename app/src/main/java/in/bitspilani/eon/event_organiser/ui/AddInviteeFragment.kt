package `in`.bitspilani.eon.event_organiser.ui

import `in`.bitspilani.eon.BitsEonActivity
import `in`.bitspilani.eon.R
import `in`.bitspilani.eon.event_organiser.models.Data
import `in`.bitspilani.eon.event_organiser.models.Invitee
import `in`.bitspilani.eon.event_organiser.viewmodel.AddInviteeViewModel
import `in`.bitspilani.eon.utils.*
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
import com.hootsuite.nachos.validator.ChipifyingNachoValidator
import kotlinx.android.synthetic.main.fragment_add_invitee.*
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt


/**
 * A simple [Fragment] subclass.
 *
 */
class AddInviteeFragment(
    private val eventData: Data,
    private val callbackListener: CallbackListener
) : DialogFragment() {

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

        setUpClickListeners()
        setObservables()

    }

    private fun setObservables() {

        addInviteeViewModel.addInviteeLiveData.observe(viewLifecycleOwner, Observer {

            dismiss()
            if (it.message.toLowerCase(Locale.ROOT).contains("success")){
                shoSnackBar("Invitee added successfully.")
                callbackListener.onDataReceived(it.data.invitee_list)
            }
            else
                shoSnackBar(it.message)


        })

        addInviteeViewModel.progressLiveData.observe(viewLifecycleOwner, Observer {

            progress.goneUnless(it)
        })

        nacho_text_view.addChipTerminator(',', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_ALL)
        nacho_text_view.addChipTerminator(
            '\n',
            ChipTerminatorHandler.BEHAVIOR_CHIPIFY_TO_TERMINATOR
        )
        nacho_text_view.addChipTerminator(
            ' ',
            ChipTerminatorHandler.BEHAVIOR_CHIPIFY_TO_TERMINATOR
        )

        nacho_text_view.chipifyAllUnterminatedTokens()

        nacho_text_view.setNachoValidator(ChipifyingNachoValidator())
    }

    private fun shoSnackBar(message: String) {

        view?.showSnackbar(message, 0)

    }

    private fun setUpClickListeners() {
        text_updated_fees.text = eventData.subscription_fee.toString()
        btn_close.clickWithDebounce { dismiss() }
        btn_invitee_cancel.clickWithDebounce { dismiss() }
        btn_invitee_confirm.clickWithDebounce {

            nacho_text_view.chipifyAllUnterminatedTokens()
            val listOfEmail = ArrayList<String>()
            for (chip in nacho_text_view.allChips) {
                listOfEmail.add(chip.text.toString())
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(chip.text.toString())
                        .matches()
                ) {
                    listOfEmail.clear()
                    shoSnackBar("Please enter a valid email address")
                    return@clickWithDebounce
                }

            }
            if (listOfEmail.size > 10) {
                shoSnackBar("Max 10 email ids are allowed.")
                return@clickWithDebounce
            }

            val array = JsonArray()
            for (each in listOfEmail) {
                array.add(each)
            }
            Timber.e("invitee_list$array")
            addInviteeViewModel.adInvitee(
                eventData.id,
                if (edt_discount.text.toString().isEmpty()) null else edt_discount.text.toString()
                    .toInt(),
                array
            )

        }

        edt_discount.onChange {
            if (it.isNotEmpty()) {

                val fees = eventData.subscription_fee - (it.toDouble()
                    .roundToInt() * (eventData.subscription_fee / 100))
                text_updated_fees.text = fees.toString()
            } else {

                text_updated_fees.text = eventData.subscription_fee.toString()
            }
        }


    }




}

interface CallbackListener {
    fun onDataReceived(inviteeList: ArrayList<Invitee>)
}
