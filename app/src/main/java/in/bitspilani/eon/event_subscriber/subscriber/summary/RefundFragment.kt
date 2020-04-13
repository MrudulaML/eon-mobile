package `in`.bitspilani.eon.event_subscriber.subscriber.summary

import `in`.bitspilani.eon.R
import `in`.bitspilani.eon.utils.Constants
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.event_summary_fragment.*
import kotlinx.android.synthetic.main.fragment_redund.*
import kotlinx.android.synthetic.main.layout_attendees_amount.*

class RefundFragment : Fragment() {

    var noOfTickets: Int = 0
    var attendees: Int = 0
    var amount: Int = 0
    var discountAmount: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_redund, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setValues()
    }
    fun setValues() {

        noOfTickets = arguments!!.getInt(Constants.NUMBER_OF_TICKETS_BOUGHT, 0)

        attendees = arguments!!.getInt(Constants.ATTENDEES, 0)

        amount = arguments!!.getInt(Constants.AMOUNT, 0)
        discountAmount = arguments!!.getInt(Constants.DISCOUNT_AMOUNT, 0)


        tv_attendees_value.text = attendees.toString()
        tv_amount_value.text = calculateFinalAmount().toString()

        tv_decreasing_attendees.text = "No. of Attendees have been updated to " + attendees

        tv_decreasing_amount.text =
            "Total amount paid for " + noOfTickets + " tickets Rs." + noOfTickets*(amount-discountAmount)

         tv_refund_amount.text = "Total amount refundable to you Rs." +  (((amount-discountAmount)*noOfTickets)-((amount-discountAmount)-attendees))


    }


    fun calculateFinalAmount(): Int {

        return ((amount - discountAmount) * attendees)
    }
}
