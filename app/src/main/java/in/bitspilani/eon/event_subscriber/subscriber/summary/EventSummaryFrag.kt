package `in`.bitspilani.eon.event_subscriber.subscriber.summary

import `in`.bitspilani.eon.R
import `in`.bitspilani.eon.event_subscriber.subscriber.detail.EventDetailsViewModel
import `in`.bitspilani.eon.utils.Constants
import `in`.bitspilani.eon.utils.clickWithDebounce
import `in`.bitspilani.eon.utils.getViewModelFactory
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.event_summary_fragment.*
import kotlinx.android.synthetic.main.layout_attendees_amount.*


class EventSummaryFrag : Fragment() {

    private val eventSummaryViewModel by viewModels<EventSummaryViewModel> { getViewModelFactory() }

    var amount: Int = 0
    var discountPercentage: Int = 0
    var discountAmount: Int = 0
    var isUpdate: Boolean = false
    var totalAttendees: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.event_summary_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setClicks()

        getAndSetValues()
    }

    fun getAndSetValues() {

        totalAttendees = arguments!!.getInt(Constants.ATTENDEES, 0)
        tv_attendees_value.text = totalAttendees.toString()
        amount = (arguments!!.getInt(Constants.AMOUNT, 0)*totalAttendees)
        tv_amount_value.text = amount.toString()
        discountPercentage = arguments!!.getInt(Constants.PROMOCODE, 0)
        discountAmount = (arguments!!.getInt(Constants.DISCOUNT_AMOUNT, 0)*totalAttendees)
        isUpdate = arguments!!.getBoolean(Constants.IS_UPDATE, false)


        cl_normal_summary.visibility = View.VISIBLE

        tv_total_amount.text = "Total Amount    ₹ " + amount
        tv_payable_amount.text = "Payable Amount    ₹  " + amount


    }


    fun setClicks() {

        btn_pay.clickWithDebounce {

            var bundle = bundleOf(
                Constants.EVENT_ID to arguments?.getInt("event_id", 0),
                "no_of_tickets" to totalAttendees,
                Constants.AMOUNT to amount,
                Constants.DISCOUNT_AMOUNT to discountAmount
            )

            Log.e("xoxo", "bundle from eventsummary: " + bundle)

            findNavController().navigate(R.id.action_summery_to_payment, bundle)
        }

        iv_cancel_promo.clickWithDebounce {

            var a = ""
            et_promocode.text = a

            tv_apply.visibility = View.VISIBLE
            iv_cancel_promo.visibility = View.GONE
            tv_payable_amount.text = "Payable Amount  ₹" + amount
            tv_discounted_amount.visibility = View.GONE
        }

        tv_apply.clickWithDebounce {

            if (discountPercentage == 0)
                showUserMsg("Promocode not available")
            else {

                var a = discountPercentage.toString() + "% Applied"
                et_promocode.text = a
                tv_discounted_amount.text = "Discount Amount -₹" + discountAmount.toString()


                tv_payable_amount.text = "Payable Amount  ₹" + (amount - discountAmount).toString()
                tv_discounted_amount.visibility = View.VISIBLE
                tv_apply.visibility = View.GONE
                iv_cancel_promo.visibility = View.VISIBLE
            }

        }
    }

    fun showUserMsg(msg: String) {
        Toast.makeText(activity, msg, Toast.LENGTH_LONG).show()
    }
}
