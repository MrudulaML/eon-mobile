package `in`.bitspilani.eon.event_subscriber.subscriber.payments

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import `in`.bitspilani.eon.R
import `in`.bitspilani.eon.utils.clickWithDebounce
import android.app.AlertDialog
import android.widget.Toast
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.layout_dialog_payment_success.view.*
import kotlinx.android.synthetic.main.payment_fragment.*

class PaymentFrag : Fragment() {

    companion object {
        fun newInstance() = PaymentFrag()
    }

    private lateinit var viewModel: PaymentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.payment_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(PaymentViewModel::class.java)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_proceed.setOnClickListener {

            if (et_card_owner_name.text.isEmpty()) {

                Toast.makeText(activity, "Please enter Card owner's name", Toast.LENGTH_SHORT)
                    .show()
            } else if (et_card_number.text.isEmpty()) {

                Toast.makeText(activity, "Please enter Card number", Toast.LENGTH_SHORT).show()

            } else if (et_expiry_date.text.isEmpty()) {
                Toast.makeText(activity, "Please enter expiry date", Toast.LENGTH_SHORT).show()

            } else {
                showSuccessDialog()
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

}
