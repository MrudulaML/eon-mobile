package `in`.bitspilani.eon.event_subscriber.subscriber.summary

import `in`.bitspilani.eon.R
import `in`.bitspilani.eon.utils.clickWithDebounce
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.event_summary_fragment.*


class EventSummaryFrag : Fragment() {


    private lateinit var viewModel: EventSummaryViewModel

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

        btn_pay.clickWithDebounce {

            findNavController().navigate(R.id.action_summery_to_payment)
        }

        iv_cancel_promo.clickWithDebounce {

            var a=""
            et_promocode.text=a

            tv_apply.visibility=View.VISIBLE
            iv_cancel_promo.visibility=View.GONE
            tv_payable_amount.text="Payable Amount  ₹"+arguments?.getInt("amount",0)
            tv_discounted_amount.visibility=View.GONE
        }

        tv_apply.clickWithDebounce {

            var a="15% Applied"
            et_promocode.text=a

            tv_discounted_amount.text="Discount Amount -₹"+arguments?.getInt("disc_amount",0)
            tv_payable_amount.text="Payable Amount  ₹ 1325"
            tv_discounted_amount.visibility=View.VISIBLE
            tv_apply.visibility=View.GONE
            iv_cancel_promo.visibility=View.VISIBLE
        }
    }


}
