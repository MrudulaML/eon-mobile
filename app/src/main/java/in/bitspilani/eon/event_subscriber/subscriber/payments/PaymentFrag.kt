package `in`.bitspilani.eon.event_subscriber.subscriber.payments

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import `in`.bitspilani.eon.R
import `in`.bitspilani.eon.event_subscriber.subscriber.detail.EventDetailsViewModel
import `in`.bitspilani.eon.login.data.Data
import `in`.bitspilani.eon.utils.*
import android.app.AlertDialog
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.layout_dialog_payment_success.view.*
import kotlinx.android.synthetic.main.payment_fragment.*

class PaymentFrag : Fragment() {

    private val paymentViewModel by viewModels<PaymentViewModel> { getViewModelFactory() }


    var hashMap: HashMap<String, Any> = HashMap()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.payment_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setObservables()
        getDataFromArgs()
        setClicks()

    }


    fun setObservables() {

        paymentViewModel.subscriptionData.observe(viewLifecycleOwner, Observer {

            showSuccessDialog()
        })

        paymentViewModel.errorData.observe(viewLifecycleOwner, Observer {


        })

    }

    fun getDataFromArgs() {

        hashMap.put("event_id", arguments!!.getInt("event_id", 0).toString())


        val userData = ModelPreferencesManager.get<Data>(Constants.CURRENT_USER)

        hashMap.put("user_id", userData?.user?.user_id as Any)
        hashMap.put("no_of_tickets", arguments!!.getInt("no_of_tickets", 0).toString())
        hashMap.put("amount", arguments!!.getInt("amount", 0).toString())
        hashMap.put("discount_amount", arguments!!.getInt("discount_amount", 0).toString())
    }

    fun setClicks() {

        btn_proceed.setOnClickListener {
            if (et_card_owner_name.text.isEmpty())
                showUserMsg("Please enter Card owner's name")
            else if (et_card_number.text.isEmpty())
                showUserMsg("Please enter Card number")
            else if (!Validator.isCardValid(et_card_number.text.toString()))
                showUserMsg("please enter valid card number")
            else if (et_expiry_date.text.isEmpty()) {
                showUserMsg("Please enter expiry date")
            } else {

                var expiryDate = et_expiry_date.text.toString()

                hashMap.put("card_number", et_card_number.text.toString())
                hashMap.put("expiry_month", expiryDate.substring(0, 2))
                hashMap.put("expiry_year", "20" + expiryDate.substring(2, 4))

                paymentViewModel.payAndSubscribe(hashMap)
            }

        }
    }

    fun showSuccessDialog() {
        //Inflate the dialog with custom view
        val mDialogView =
            LayoutInflater.from(activity).inflate(R.layout.layout_dialog_payment_success, null)
        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(activity)
            .setView(mDialogView)
        //show dialog
        val mAlertDialog = mBuilder.show()

        mDialogView.btn_okay.clickWithDebounce {

            findNavController().navigate(
                R.id.action_payment_to_homeFragment,
                null,
                NavOptions.Builder()
                    .setPopUpTo(
                        R.id.homeFragment,
                        true
                    ).build()
            )

            mAlertDialog.dismiss()
        }

    }

    fun showUserMsg(msg: String) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT)
            .show()
    }


}
