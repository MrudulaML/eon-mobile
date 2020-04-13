package `in`.bitspilani.eon.event_subscriber.subscriber.detail

import `in`.bitspilani.eon.R
import `in`.bitspilani.eon.databinding.EventDetailsFragmentBinding
import `in`.bitspilani.eon.event_subscriber.models.Data
import `in`.bitspilani.eon.login.ui.ActionbarHost
import `in`.bitspilani.eon.utils.*
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
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.event_details_fragment.*
import kotlinx.android.synthetic.main.layout_cancel_event.view.*
import kotlinx.android.synthetic.main.layout_dialog_payment_success.view.*
import kotlinx.android.synthetic.main.layout_download_cancel_button.*
import kotlinx.android.synthetic.main.layout_email_share_to_friend.view.*
import kotlinx.android.synthetic.main.layout_seat_booking.*
import java.lang.Exception


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

        try {


            eventDetailsFragmentBinding.viewmodel = eventDetailsViewModel
            setObservables()

            eventDetailsViewModel.getEventDetails(arguments!!.getInt(Constants.EVENT_ID, 0))

            actionbarHost?.showToolbar(showToolbar = false, showBottomNav = false)


            tv_seat_counter.text = 1.toString()

            setClicks()
        } catch (e: Exception) {

            Log.e("EventDetailsTag", e.toString())
        }
    }

    fun setClicks() {


        btn_price.clickWithDebounce {

            var noOfTickets = data.subscription_details!!.no_of_tickets_bought
            if (count == noOfTickets) {

                showUserMsg("Please change your current seats")

            } else {
                var bundle =
                    bundleOf(

                        Constants.EVENT_ID to data.event_id,
                        Constants.AMOUNT to amount,
                        Constants.DISCOUNT_AMOUNT to calculateDiscount(),
                        Constants.ATTENDEES to count,
                        Constants.PROMOCODE to data.discountPercentage,
                        Constants.IS_UPDATE to isSubscribed

                    )

                if (count < noOfTickets) {

                    bundle.putInt(
                        Constants.NUMBER_OF_TICKETS_BOUGHT,
                        data.subscription_details!!.no_of_tickets_bought
                    )

                    findNavController().navigate(R.id.refundFragment, bundle)

                } else {

                    if (isSubscribed) {
                        bundle.putInt(Constants.ATTENDEES, count - noOfTickets)
                        bundle.putInt(Constants.AMOUNT, amount)

                    } else if (data.subscription_fee == 0) {

                        subscribeToFreeEvent()
                    } else
                        findNavController().navigate(R.id.eventSummaryFrag, bundle)
                }

                Log.e("xoxo", "bundle from eventdetails " + bundle)


            }

        }

        iv_share.clickWithDebounce {

            showEmailDialog()
        }

        btn_cancel.clickWithDebounce {

            showCancelEventDialog()
        }


    }


    fun calculateDiscount(): Int {

        if (data.discountPercentage == 0) {
            return 0
        }

        return (amount * (data.discountPercentage / 100))

    }

    fun subscribeToFreeEvent() {

        var map: HashMap<String, Any> = HashMap()
        map.put(Constants.EVENT_ID, data.event_id)
        val userData =
            ModelPreferencesManager.get<`in`.bitspilani.eon.login.data.Data>(Constants.CURRENT_USER)
        map.put(Constants.USER_ID, userData!!.user.user_id)
        map.put(Constants.NUMBER_OF_TICKETS, count)

        eventDetailsViewModel.subscribeToFreeEvent(map)

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

    var isSubscribed: Boolean = false

    fun setObservables() {


        //event detail observer
        eventDetailsViewModel.eventData.observe(viewLifecycleOwner, Observer {

            eventDetailsFragmentBinding.eventData = it.data
            amount = it.data.subscription_fee
            data = it.data
            btn_price.text = "₹ $amount"



            it.data.subscription_details.let {

                if (it!!.is_subscribed) {
                    tv_subscribed_event_text.visibility = View.VISIBLE
                    download_cancel_view.visibility = View.VISIBLE
                    isSubscribed = true
                    btn_price.text = "Update"
                    count = it.no_of_tickets_bought
                    seatCount.postValue(count)
                }

            }

            showUserMsg(it.message)

        })

        //wishlist observer

        eventDetailsViewModel.wishlistData.observe(viewLifecycleOwner, Observer {

            showUserMsg(it)

        })


        //counter observer
        seatCount.observe(this, Observer {

            tv_seat_counter.text = it.toString()
            if (!isSubscribed) {
                btn_price.text = "₹ " + (it * amount)

            }
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

        eventDetailsViewModel.freeEventLiveData.observe(viewLifecycleOwner, Observer {

            SuccessDialog.openDialog(activity!!) {

                findNavController().navigate(R.id.action_eventDetails_to_Homefragment)
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


    fun showCancelEventDialog() {

        //Inflate the dialog with custom view
        val mDialogView =
            LayoutInflater.from(activity).inflate(R.layout.layout_cancel_event, null)
        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(activity)
            .setView(mDialogView)
        //show dialog
        mAlertDialog = mBuilder.show()

        mDialogView.btn_confirm.clickWithDebounce {

            eventDetailsViewModel.cancelEvent(data.event_id)
        }

    }
}

