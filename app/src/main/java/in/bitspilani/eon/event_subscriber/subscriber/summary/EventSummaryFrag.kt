package `in`.bitspilani.eon.event_subscriber.subscriber.summary

import `in`.bitspilani.eon.R
import `in`.bitspilani.eon.login.ui.ActionbarHost
import `in`.bitspilani.eon.utils.Constants
import `in`.bitspilani.eon.utils.clickWithDebounce
import `in`.bitspilani.eon.utils.getViewModelFactory
import `in`.bitspilani.eon.utils.goneUnless
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.event_summary_fragment.*
import kotlinx.android.synthetic.main.layout_attendees_amount.*


class EventSummaryFrag : Fragment() {

    private val eventSummaryViewModel by viewModels<EventSummaryViewModel> { getViewModelFactory() }
    private var actionbarHost: ActionbarHost? = null

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

        actionbarHost?.showToolbar(showToolbar = false, showBottomNav = false)

        setClicks()

        getAndSetValues()
    }

    fun getAndSetValues() {


        totalAttendees = arguments!!.getInt(Constants.ATTENDEES, 0)
        tv_attendees_value.text = totalAttendees.toString()
        amount = (arguments!!.getInt(Constants.AMOUNT, 0) * totalAttendees)
        tv_amount_value.text = amount.toString()
        discountPercentage = arguments!!.getInt(Constants.PROMOCODE, 0)
        discountAmount = (arguments!!.getInt(Constants.DISCOUNT_AMOUNT, 0) * totalAttendees)
        isUpdate = arguments!!.getBoolean(Constants.IS_UPDATE, false)

        et_promocode.goneUnless(discountPercentage > 0)
        tv_apply.goneUnless(discountPercentage > 0)

        tv_promocode.goneUnless(discountPercentage > 0)
        et_promocode.text=  discountPercentage.toString() + "% discount available"

        cl_normal_summary.visibility = View.VISIBLE

        tv_total_amount.text = "Total Amount    ₹ " + amount
        tv_payable_amount.text = "Payable Amount    ₹  " + amount

    }


    fun setClicks() {

        iv_back.clickWithDebounce { findNavController().popBackStack() }

        btn_pay.clickWithDebounce {

            var bundle = bundleOf(
                Constants.EVENT_ID to arguments?.getInt("event_id", 0),
                "no_of_tickets" to totalAttendees,
                Constants.AMOUNT to (amount - discountAmount),
                Constants.DISCOUNT_AMOUNT to discountAmount
            )


            Log.e("xoxo", "bundle from eventsummary: " + bundle)

            findNavController().navigate(R.id.action_summery_to_payment, bundle)
        }


        tv_apply.clickWithDebounce {

            if (discountPercentage == 0)
                showUserMsg("Promocode not available")
            else {


                var a = discountPercentage.toString() + "% Applied"
                et_promocode.text = a
                tv_discounted_amount.text =
                    "Discount Amount -₹" + discountAmount

                tv_payable_amount.text =
                    "Payable Amount  ₹" + (amount - discountAmount)
                tv_discounted_amount.visibility = View.VISIBLE
                tv_apply.visibility = View.GONE
            }

        }
    }

    fun showUserMsg(msg: String) {
        Toast.makeText(activity, msg, Toast.LENGTH_LONG).show()
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
