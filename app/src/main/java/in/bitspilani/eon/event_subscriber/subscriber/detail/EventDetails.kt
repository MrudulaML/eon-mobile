package `in`.bitspilani.eon.event_subscriber.subscriber.detail

import `in`.bitspilani.eon.R
import `in`.bitspilani.eon.databinding.EventDetailsFragmentBinding
import `in`.bitspilani.eon.event_subscriber.models.Data
import `in`.bitspilani.eon.login.ui.ActionbarHost
import `in`.bitspilani.eon.utils.*
import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
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
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.event_details_fragment.*
import kotlinx.android.synthetic.main.layout_cancel_event.view.*
import kotlinx.android.synthetic.main.layout_dialog_payment_success.view.*
import kotlinx.android.synthetic.main.layout_download_cancel_button.*
import kotlinx.android.synthetic.main.layout_email_share_to_friend.view.*
import kotlinx.android.synthetic.main.layout_seat_booking.*
import android.graphics.pdf.PdfDocument
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Build
import android.os.Environment
import androidx.navigation.NavOptions
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception


class EventDetails : Fragment() {

    private val STORAGE_PERMISSION_CODE: Int = 1000

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

    var noOfTickets: Int = 0

    fun setClicks() {


        btn_price.clickWithDebounce {

            noOfTickets = data.subscription_details!!.no_of_tickets_bought
            if (count == noOfTickets) {

                showUserMsg("Please change your current seats")

            } else if (data.subscription_fee == 0) {

                subscribeToFreeEvent()

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

                    bundle.putInt(Constants.AMOUNT_PAID, data!!.subscription_details!!.amount_paid)
                    bundle.putInt(
                        Constants.DISCOUNT_GIVEN,
                        data!!.subscription_details!!.discount_given
                    )

                    bundle.putInt(
                        Constants.NUMBER_OF_TICKETS_BOUGHT,
                        data.subscription_details!!.no_of_tickets_bought
                    )

                    findNavController().navigate(R.id.refundFragment, bundle)

                } else {

                    if (isSubscribed) {
                        bundle.putInt(Constants.ATTENDEES, count - noOfTickets)
                        bundle.putInt(Constants.AMOUNT, amount)

                        findNavController().navigate(R.id.eventSummaryFrag, bundle)

                        Log.e("xoxo", "bundle sent to payments" + bundle)

                    } else {
                        Log.e("xoxo", "bundle sent to summary" + bundle)

                        findNavController().navigate(R.id.eventSummaryFrag, bundle)
                    }

                }

            }
        }

        iv_share.clickWithDebounce {

            SendEmailDialog.openDialog(activity!!) { emailId, message ->

                var hashMap: HashMap<String, Any> = HashMap()
                hashMap.put("email_id", emailId)
                hashMap.put("message", message)

                eventDetailsViewModel.sendEmail(hashMap)

            }
        }

        btn_cancel.clickWithDebounce {

            CancelDialog.openDialog(activity!!) {

                eventDetailsViewModel.cancelEvent(data.event_id)
            }
        }

        // button download
        btn_download.clickWithDebounce {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (activity?.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    requestPermissions(
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        STORAGE_PERMISSION_CODE
                    )
                } else {
                    createPdf()
                }
            } else {

            }
        }

        iv_external_share.clickWithDebounce {

            CommonUtil.openLinkBrowser(activity!!, data.external_links)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            STORAGE_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    createPdf()
                } else {
                    showUserMsg("Permission denied")
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    fun calculateDiscount(): Int {

        if (data.discountPercentage == 0) {
            return 0
        }



        return ((data.discountPercentage.toDouble() / 100) * amount).toInt()

    }

    fun subscribeToFreeEvent() {

        var map: HashMap<String, Any> = HashMap()
        map.put(Constants.EVENT_ID, data.event_id)
        val userData =
            ModelPreferencesManager.get<`in`.bitspilani.eon.login.data.Data>(Constants.CURRENT_USER)

        map.put(Constants.USER_ID, userData!!.user.user_id)


        if (noOfTickets == 0)
            map.put(Constants.NUMBER_OF_TICKETS, count)
        else
            map.put(Constants.NUMBER_OF_TICKETS, count - noOfTickets)


        eventDetailsViewModel.subscribeToFreeEvent(map)

    }

    var count = 1
    fun setDummyCounterLogic() {


        iv_increment.setOnClickListener {

            seatCount.postValue(++count)

        }

        iv_decrement.setOnClickListener {

            if (count > 0) seatCount.postValue(--count)

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

           if(amount>0) btn_price.text = "₹ $amount" else btn_price.text="confirm"

            if (data.is_wishlisted) {
                eventDetailsViewModel.wishlist = false
                iv_wishlist.src(R.drawable.ic_wishlist_fill)
            }
            tv_event_date.formatDate(data.date, data.time)

            if (data.is_subscribed) {
                isSubscribed = true
                ll_paid_event_info.visibility = View.VISIBLE
                tv_seats_info.text = "You already bought " + data.subscription_details!!.no_of_tickets_bought + "tickets for this event"

                if(data.subscription_details!!.amount_paid>0)
                tv_total_amount_paid.text = "Total amount paid: ₹" + data.subscription_details!!.amount_paid

                tv_total_amount_paid.goneUnless(data.subscription_details!!.amount_paid>0)

                download_cancel_view.visibility = View.VISIBLE
                btn_price.text = "Update"

                it.data.subscription_details.let {

                    count = it!!.no_of_tickets_bought
                    seatCount.postValue(count)

                }
            }

            showUserMsg(it.message)

        })

        //wishlist observer

        eventDetailsViewModel.wishlistData.observe(viewLifecycleOwner, Observer {

            showUserMsg(it)

            if (!eventDetailsViewModel.wishlist)
                iv_wishlist.src(R.drawable.ic_wishlist_fill)
            else
                iv_wishlist.src(R.drawable.ic_wishlist_line)

        })


        //counter observer
        seatCount.observe(this, Observer {


            tv_seat_counter.text = it.toString()
            if (!isSubscribed) {
                if(amount>0)
                btn_price.text = "₹ " + (it * amount)

            }
        })

        //send email observer

        eventDetailsViewModel.emailApiData.observe(viewLifecycleOwner, Observer {

            showUserMsg(it)


            SendEmailDialog.dismissDialog()


        })

        eventDetailsViewModel.errorView.observe(viewLifecycleOwner, Observer {

            showUserMsg(it)

        })

        eventDetailsViewModel.shareEmailError.observe(viewLifecycleOwner, Observer {

            showUserMsg(it)
            SendEmailDialog.dismissDialog()

        })

        eventDetailsViewModel.freeEventLiveData.observe(viewLifecycleOwner, Observer {

            SuccessDialog.openDialog(activity!!) {

                findNavController().navigate(
                    R.id.action_eventDetails_to_eventDetails,
                    bundleOf(Constants.EVENT_ID to data.event_id),
                    NavOptions.Builder().setPopUpTo(R.id.eventDetails, true).build()
                )
            }

        })

        eventDetailsViewModel.cancelEventData.observe(viewLifecycleOwner, Observer {

            showUserMsg(it)
            findNavController().navigate(R.id.action_eventDetails_to_Homefragment)

        })

    }

    fun showUserMsg(msg: String) {
        Toast.makeText(activity, msg, Toast.LENGTH_LONG).show()
    }

    // create pdf and save external directory
    private fun createPdf() {

        val eventName = tv_event_name.text.toString()
        val eventDateTime = tv_event_date.text.toString()
        val eventLocation = tv_event_location.text.toString()
        val eventSeatCounter = tv_seat_counter.text.toString()
        val eventAmount = btn_price.text.toString()

        val document = PdfDocument()

        val pageInfo = PdfDocument.PageInfo.Builder(1200, 2010, 1).create()
        val page = document.startPage(pageInfo)
        val canvas = page.canvas
        val paint = Paint()
        val titlePaint = Paint()

        // header title
        titlePaint.textAlign = Paint.Align.CENTER
        titlePaint.textSize = resources.getDimension(R.dimen._20fs)
        titlePaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText(eventName, 1200 / 2F, 200F, titlePaint)

        // tickets info right side
        paint.textAlign = Paint.Align.RIGHT
        paint.textSize = resources.getDimension(R.dimen._16fs)
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        canvas.drawText("Event Name: " + eventName, 1200 - 40F, 250F, paint);
        canvas.drawText("Number of seats: " + eventSeatCounter, 1200F - 80F, 300F, paint);
        canvas.drawText("Amount: " + eventAmount, 1200 - 40F, 350F, paint);
        canvas.drawText("Event Date: " + eventDateTime, 1200 - 40F, 400F, paint);
        canvas.drawText("Location: " + eventLocation, 1200 - 40F, 450F, paint);

        document.finishPage(page)

        val directoryPath = Environment.getExternalStorageDirectory().path + "/invoices/"

        val dir = File(directoryPath)

        if (!dir.exists())
            dir.mkdirs()
        val filePath: File
        filePath = File(directoryPath, "tickets.pdf")

        if (filePath.exists()) {
            filePath.delete()
            filePath.createNewFile()
        } else {
            filePath.createNewFile()
        }

        try {
            document.writeTo(FileOutputStream(filePath))
            showUserMsg("Invoice downloaded")
            Log.e("Invoice", "Tickets" + filePath)
        } catch (e: IOException) {
            Log.e("Invoice", "Error: " + e.toString());
            showUserMsg("Error!")
        }

        document.writeTo(FileOutputStream(filePath));
        document.close();

    }


}

