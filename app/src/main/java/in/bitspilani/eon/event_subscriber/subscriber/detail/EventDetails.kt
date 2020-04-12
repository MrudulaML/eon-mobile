package `in`.bitspilani.eon.event_subscriber.subscriber.detail

import `in`.bitspilani.eon.R
import `in`.bitspilani.eon.databinding.EventDetailsFragmentBinding
import `in`.bitspilani.eon.event_subscriber.models.Data
import `in`.bitspilani.eon.login.ui.ActionbarHost
import `in`.bitspilani.eon.utils.EmailValidator
import `in`.bitspilani.eon.utils.clickWithDebounce
import `in`.bitspilani.eon.utils.getViewModelFactory
import android.app.AlertDialog
import android.content.Context
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.event_details_fragment.*
import kotlinx.android.synthetic.main.layout_dialog_payment_success.view.*
import kotlinx.android.synthetic.main.layout_email_share_to_friend.view.*
import kotlinx.android.synthetic.main.layout_seat_booking.*


class EventDetails : Fragment() {

    private val eventDetailsViewModel by viewModels<EventDetailsViewModel> { getViewModelFactory() }

    var seatCount: MutableLiveData<Int> = MutableLiveData()
    private var actionbarHost: ActionbarHost? = null

    lateinit var data: Data
    lateinit var eventDetailsFragmentBinding: EventDetailsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        eventDetailsFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.event_details_fragment, container, false)

        return eventDetailsFragmentBinding.root
    }


    var amount = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDummyCounterLogic()

        eventDetailsFragmentBinding.viewmodel = eventDetailsViewModel
        setObservables()

        eventDetailsViewModel.getEventDetails(1)

        actionbarHost?.showToolbar(showToolbar = false, showBottomNav = false)

        tv_seat_counter.text = 1.toString()

        setClicks()
    }


    fun setClicks() {

        btn_price.clickWithDebounce {

            var bundle =
                bundleOf(

                    "event_id" to data.event_id,
                    "amount" to count * amount,
                    "disc_amount " to calculateDiscount(),
                    "attendees" to count,
                    "promocode" to data.discountPercentage
                )

            Log.e("xoxo","event id from event details: "+data.event_id)
            findNavController().navigate(R.id.eventSummaryFrag, bundle)

        }

        iv_share.clickWithDebounce {

            showEmailDialog()
        }

    }


    fun calculateDiscount(): Int {

        if (data.discountPercentage == 0) {
            return 0
        }

        return (amount * (data.discountPercentage / 100))

    }

    var count = 1
    fun setDummyCounterLogic() {


        iv_increment.setOnClickListener {

            seatCount.postValue(count++)

        }

        iv_decrement.setOnClickListener {

            if (count > 0) seatCount.postValue(count--)

        }

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


    fun setObservables() {


        //event detail observer
        eventDetailsViewModel.eventData.observe(viewLifecycleOwner, Observer {

            eventDetailsFragmentBinding.eventData = it.data
            amount = it.data.subscription_fee
            data = it.data
            btn_price.text = "₹ $amount"

            showUserMsg(it.message)

        })

        //wishlist observer

        eventDetailsViewModel.wishlistData.observe(viewLifecycleOwner, Observer {

            showUserMsg(it)

        })


        //counter observer
        seatCount.observe(this, Observer {

            tv_seat_counter.text = count.toString()
            btn_price.text = "₹ " + (count * amount)
        })

        //send email observer

        eventDetailsViewModel.emailApiData.observe(viewLifecycleOwner, Observer {

            showUserMsg(it)
            if (mAlertDialog != null) {

                mAlertDialog.dismiss()
            }

        })

        eventDetailsViewModel.errorView.observe(viewLifecycleOwner, Observer {

            showUserMsg(it)
            if (mAlertDialog != null) {
                mAlertDialog.dismiss()

            }

        })
    }

    fun showUserMsg(msg: String) {
        Toast.makeText(activity, msg, Toast.LENGTH_LONG).show()
    }

    lateinit var mAlertDialog: AlertDialog

    fun showEmailDialog() {

        //Inflate the dialog with custom view
        val mDialogView =
            LayoutInflater.from(activity).inflate(R.layout.layout_email_share_to_friend, null)
        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(activity)
            .setView(mDialogView)
        //show dialog
        mAlertDialog = mBuilder.show()

        mDialogView.btn_share.clickWithDebounce {


            if (mDialogView.et_email_id.text.isEmpty())
                showUserMsg("Please enter an email id")
            if (!EmailValidator.isEmailValid(mDialogView.et_email_id.text.toString()))
                showUserMsg("Please enter a valid email id")
            else if (mDialogView.et_message.text.isEmpty())
                showUserMsg("Please enter some message")
            else {

                var hashMap: HashMap<String, Any> = HashMap()
                hashMap.put("email_id", mDialogView.et_email_id.text)
                hashMap.put("message", mDialogView.et_message.text)

                eventDetailsViewModel.sendEmail(hashMap)
            }

        }

    }
}
