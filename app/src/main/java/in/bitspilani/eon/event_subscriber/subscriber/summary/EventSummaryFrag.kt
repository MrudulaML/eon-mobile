package `in`.bitspilani.eon.event_subscriber.subscriber.summary

import `in`.bitspilani.eon.R
import `in`.bitspilani.eon.utils.clickWithDebounce
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.event_summary_fragment.*


class EventSummaryFrag : Fragment() {

    private lateinit var viewModel: EventSummaryViewModel

    var amount: Int = 0
    var discountPercentage: Int = 0
    var discountAmount: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.event_summary_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(EventSummaryViewModel::class.java)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setClicks()

        setValues()

    }

    fun setValues() {

        tv_attendees_value.text = arguments!!.getInt("attendees", 0).toString()
        amount = arguments!!.getInt("amount", 0)
        tv_amount_value.text = amount.toString()
        discountPercentage = arguments!!.getInt("promocode", 0)
        discountAmount = arguments!!.getInt("disc_amount", 0)

        tv_total_amount.text = "Total Amount    ₹ " + amount
        tv_payable_amount.text = "Payable Amount    ₹  " + amount
    }


    fun setClicks() {

        btn_pay.clickWithDebounce {

            var bundle = bundleOf(
                "event_id" to arguments?.getInt("event_id", 0),
                "no_of_tickets" to arguments!!.getInt("attendees", 0),
                "amount" to amount,
                "discount_amount" to discountAmount
            )



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
