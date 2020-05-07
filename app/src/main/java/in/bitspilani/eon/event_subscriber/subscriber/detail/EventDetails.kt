package `in`.bitspilani.eon.event_subscriber.subscriber.detail

import `in`.bitspilani.eon.BitsEonActivity
import `in`.bitspilani.eon.R
import `in`.bitspilani.eon.databinding.EventDetailsFragmentBinding
import `in`.bitspilani.eon.event_organiser.viewmodel.OrgFeedbackViewmodel
import `in`.bitspilani.eon.event_subscriber.models.Data
import `in`.bitspilani.eon.login.ui.ActionbarHost
import `in`.bitspilani.eon.utils.*
import android.Manifest
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.facebook.CallbackManager
import com.facebook.share.model.ShareLinkContent
import com.facebook.share.widget.ShareDialog
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlinx.android.synthetic.main.event_details_fragment.*
import kotlinx.android.synthetic.main.layout_download_cancel_button.*
import kotlinx.android.synthetic.main.layout_seat_booking.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap


class EventDetails : Fragment() {

    private val STORAGE_PERMISSION_CODE: Int = 1000
    private var bitmap: Bitmap? = null

    private val eventDetailsViewModel by viewModels<EventDetailsViewModel> { getViewModelFactory() }

    private val orgFeedbackViewmodel by viewModels<OrgFeedbackViewmodel> { getViewModelFactory() }

    var seatCount: MutableLiveData<Int> = MutableLiveData()
    private var actionbarHost: ActionbarHost? = null

    lateinit var data: Data
    lateinit var eventDetailsFragmentBinding: EventDetailsFragmentBinding

    var shareDialog: ShareDialog? = null
    var callbackManager: CallbackManager? = null

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
            callbackManager = CallbackManager.Factory.create()
            shareDialog = ShareDialog(this)

            eventDetailsFragmentBinding.viewmodel = eventDetailsViewModel
            setObservables()

            eventDetailsViewModel.getEventDetails(arguments!!.getInt(Constants.EVENT_ID, 0))

            actionbarHost?.showToolbar(showToolbar = false, showBottomNav = false)


            tv_seat_counter.text = 1.toString()

            btn_price.transformationMethod = null
            btn_feedback.transformationMethod=null

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
                       bundle.putInt( Constants.PROMOCODE,  data!!.subscription_details!!.discount_percentage)

                        bundle.putInt(Constants.DISCOUNT_AMOUNT , calculateDiscount())

                    }

                    findNavController().navigate(R.id.action_eventDetails_to_summary, bundle)


                }

            }
        }

        iv_share.clickWithDebounce {

            SendEmailDialog.openDialog(activity!!) { emailId, message ->

                var hashMap: HashMap<String, Any> = HashMap()
                hashMap.put("email_id", emailId)
                hashMap.put("message", message)
                hashMap.put("event_id", data.event_id)

                eventDetailsViewModel.sendEmail(hashMap)

            }
        }


        iv_facebook.clickWithDebounce {
            shareFacebook()
        }
        btn_cancel.clickWithDebounce {

            CancelDialog.openDialog(activity!!) {

                eventDetailsViewModel.cancelEvent(data.event_id)
            }
        }

        iv_back.clickWithDebounce {

            findNavController().popBackStack()
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
                createPdf()
            }
        }

        iv_external_share.clickWithDebounce {

            try {

                CommonUtil.openLinkBrowser(activity!!, data.external_links)

            }catch (e:Exception){

            }
        }

        btn_feedback.clickWithDebounce {

            if (!isFeedbackGiven) {
                findNavController().navigate(
                    R.id.action_eventDetails_to_subscriberFeedback,
                    bundleOf(
                        Constants.EVENT_ID to data.event_id,
                        Constants.EVENT_NAME to data.event_name
                    )
                )
            } else {

                orgFeedbackViewmodel.getUsers(data.event_id)

            }

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

        if(isSubscribed){

            if( data!!.subscription_details!!.discount_percentage==0)
                return 0
            else{

                return ((data!!.subscription_details!!.discount_percentage.toDouble() / 100) * amount).toInt()
            }

        }

        else  if (data.discountPercentage == 0) {
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

    var allowBooking: Boolean = true

    var count = 1
    fun setDummyCounterLogic() {


        iv_increment.setOnClickListener {


            if (count < data.remainingTickets + data.subscription_details!!.no_of_tickets_bought) {

                seatCount.postValue(++count)

                changePriceButtonColor()
            } else {

                showUserMsg("Only " + data.remainingTickets + " tickets are remaining")
            }


        }

        iv_decrement.setOnClickListener {

            if (count > 1)
                seatCount.postValue(--count)

            changePriceButtonColor()
        }

    }

    fun changePriceButtonColor() {

        if (!isCountAndBoughtTicketsSame()) {
            btn_price.setBackgroundResource(R.drawable.bg_blue_cruve)
            btn_price.setTextColor(activity!!.resources.getColor(R.color.white))
        } else {
            btn_price.setBackgroundResource(R.drawable.bg_grey_curve)
            btn_price.setTextColor(activity!!.resources.getColor(R.color.black))
        }


    }

    fun isCountAndBoughtTicketsSame(): Boolean {

        if (data.subscription_details != null) {

            return count == data.subscription_details!!.no_of_tickets_bought
        } else
            return false

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ActionbarHost) {
            actionbarHost = context
        }
    }

    var isSubscribed: Boolean = false

    var isFeedbackGiven: Boolean = false

    fun setObservables() {


        //event detail observer
        eventDetailsViewModel.eventData.observe(viewLifecycleOwner, Observer {

            eventDetailsFragmentBinding.eventData = it.data
            amount = it.data.subscription_fee
            data = it.data
            it.data.images?.let {

                loadImage(iv_event, it)
            }

            included_seat_counter.goneUnless(data.eventStatus.equals("upcoming"))
            btn_cancel.goneUnless(data.eventStatus.equals("upcoming"))

            setEventStatus(tv_event_status, data.eventStatus)

            getType(tv_type_of_event_value,data.event_type)

            if (amount > 0) btn_price.text = "₹ $amount" else btn_price.text = "Confirm"



            if (data.feedback_given) {
                isFeedbackGiven = true

                btn_feedback.text = "View Feedback"
            }

            if (data.is_wishlisted) {
                eventDetailsViewModel.wishlist = false
                iv_wishlist.src(R.drawable.ic_wishlist_fill)
            }
           // tv_event_date.formatDate(data.date, data.time)

           convertDateAndTime(tv_event_date,data.date+" "+data.time)
            if (data.is_subscribed) {
                btn_feedback.visibility = View.VISIBLE
                isSubscribed = true
                ll_paid_event_info.visibility = View.VISIBLE
                tv_seats_info.text =
                    "You have already bought " + data.subscription_details!!.no_of_tickets_bought + " seats for this event"

                if (data.subscription_details!!.amount_paid > 0)
                    tv_total_amount_paid.text =
                        "Total amount paid: ₹" + data.subscription_details!!.amount_paid

                tv_total_amount_paid.goneUnless(data.subscription_details!!.amount_paid > 0)

                download_cancel_view.visibility = View.VISIBLE
                btn_price.text = "Update"


                it.data.subscription_details.let {

                    count = it!!.no_of_tickets_bought
                    seatCount.postValue(count)

                    changePriceButtonColor()
                }
            }else{

                count=1
                seatCount.postValue(count)
            }

        })


        eventDetailsViewModel.errorView.observe(viewLifecycleOwner, Observer {

            showUserMsg(it)
        })

        //wishlist observer

        eventDetailsViewModel.wishlistData.observe(viewLifecycleOwner, Observer {

            showUserMsg(it)

            if (!eventDetailsViewModel.wishlist)
                iv_wishlist.src(R.drawable.ic_wishlist_fill)
            else
                iv_wishlist.src(R.drawable.ic_wishlist_line)

        })


        orgFeedbackViewmodel.feedbackListData.observe(viewLifecycleOwner, Observer {

            var gson = Gson()

            findNavController().navigate(
                R.id.action_subsEventDetails_to_orgFeedbackDetail,
                bundleOf(Constants.RESPONSE_LIST to gson.toJson(it.data[0]).toString())
            )

        })

        //counter observer
        seatCount.observe(viewLifecycleOwner, Observer {

            tv_seat_counter.text = it.toString()
            if (!isSubscribed) {
                if (amount > 0)
                    btn_price.text = "₹ " + (it * amount)

            }

        })

        //send email observer

        eventDetailsViewModel.emailApiData.observe(viewLifecycleOwner, Observer {

            showUserMsg(it)


            SendEmailDialog.dismissDialog()


        })

        eventDetailsViewModel.progressLiveData.observe(viewLifecycleOwner, Observer {

            (activity as BitsEonActivity).showProgress(it)
        })

        eventDetailsViewModel.errorToast.observe(viewLifecycleOwner, Observer {

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

        try {
            val eventId = Integer.toString(data.event_id) // event id

            val userData =
                ModelPreferencesManager.get<`in`.bitspilani.eon.login.data.Data>(Constants.CURRENT_USER)

            val multiFormatWriter = MultiFormatWriter()
            var barcodeEncoder = BarcodeEncoder()
            var bitMatrix: BitMatrix? = null


            val orgTitle = "EOn"
            val eventName = data.event_name
            val eventDateTime = getFormattedDate(data.date)
            val eventLocation = data.location
            val eventSeatCounter = data.subscription_details?.no_of_tickets_bought
            val eventAmount = data.subscription_details?.amount_paid.toString()

            val bookingNotes = "Ticket is non-transferable."
            val bookingDate = getFormattedDate(data.subscription_details!!.createdOn)

            var userEmailId = userData!!.user.email
            var userName = ""

            if (userData!!.user.name == null) {
                userName = userEmailId.substring(0, userEmailId.indexOf("@"));
            } else {
                userName = userData!!.user.name
            }

            val userContact = userData!!.user.contact_number

            val logoBitmap = BitmapFactory.decodeResource(resources, R.drawable.logo_bits)

            // resize logo
            val resizedLogoBitmap = Bitmap.createScaledBitmap(
                logoBitmap, 128, 128, false
            )

            val document = PdfDocument()
            val pageInfo = PdfDocument.PageInfo.Builder(1200, 2010, 1).create()
            val page = document.startPage(pageInfo)
            val canvas = page.canvas

            val bitmapPaint = Paint()
            val titlePaint = Paint()
            val linePaint = Paint()
            val textPaint = Paint()

            // logo
            canvas.drawBitmap(resizedLogoBitmap, 40F, 80F, bitmapPaint)

            // header
            titlePaint.textSize = resources.getDimension(R.dimen._22fs)
            titlePaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            canvas.drawText(orgTitle, 180F, 160F, titlePaint)// divider
            canvas.drawLine(40F, 248F, 1200 - 40F, 248F, linePaint)

            // QR Code
            try {
                bitMatrix = multiFormatWriter.encode(eventId, BarcodeFormat.QR_CODE, 240, 240)
                val myBitmap: Bitmap = barcodeEncoder.createBitmap(bitMatrix)
                canvas.drawBitmap(myBitmap, 40F, 290F, bitmapPaint)
            } catch (e: WriterException) {
                Log.e("QRCode", "Error: " + e.toString());
                showUserMsg("Error!")
            }

            // Event info
            textPaint.textSize = resources.getDimension(R.dimen._14fs)
            textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            canvas.drawText("Event Name: " + eventName, 300F, 350F, textPaint);
            canvas.drawText("Number of seats: " + eventSeatCounter, 300F, 410F, textPaint);
            canvas.drawText("Amount: " + eventAmount, 300F, 470F, textPaint);
            canvas.drawText("Event Date: " + eventDateTime, 300F, 530F, textPaint);
            canvas.drawText("Location: " + eventLocation, 300F, 590F, textPaint);
            canvas.drawText("Subscriber Name: " + userName, 300F, 650F, textPaint);
            canvas.drawText("Email Id: " + userEmailId, 300F, 710F, textPaint);
            canvas.drawText("Contact: " + userContact, 300F, 770F, textPaint);
            canvas.drawText("Booking Date: " + bookingDate, 300F, 830F, textPaint);

            // Note
            textPaint.textSize = resources.getDimension(R.dimen._14fs)
            textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            canvas.drawText("*Note: " + bookingNotes, 300F, 930F, textPaint);

            document.finishPage(page)

            val directoryPath = Environment.getExternalStorageDirectory().path + "/Download/"

            var downloadManager: DownloadManager? = null
            downloadManager = context?.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

            val dir = File(directoryPath)

            if (!dir.exists())
                dir.mkdirs()
            val filePath: File

            filePath = File(directoryPath, eventName+"-"+userName+".pdf")

            if (filePath.exists()) {
                filePath.delete()
                filePath.createNewFile()
            } else {
                filePath.createNewFile()
            }

            try {
                document.writeTo(FileOutputStream(filePath))
                // creating also in documents
                showSnackBar("Downloaded", true);
            } catch (e: IOException) {
                Log.e("Invoice", "Error: " + e.toString());
                view?.showSnackbar("Error")
            }
            document.close();

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                showSnackBar("Downloaded", true);
            } else {
                downloadManager.addCompletedDownload(
                    filePath.name,
                    filePath.name,
                    true,
                    "application/pdf",
                    filePath.absolutePath,
                    filePath.length(),
                    true
                )
            }
        } catch (e: Exception) {
            Log.e("xoxo", "external prob: " + e.toString())
            showSnackBar(e.toString(), true)

        }

    }

    fun getFormattedDate(dateInString: String): String {
        try {
            var dateFormat: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd");
            var date: Date = dateFormat.parse(dateInString);
            val formatter: DateFormat = SimpleDateFormat("EEEEEEEEE, dd MMM yyyy ")
            val today = formatter.format(date)
            return today
        } catch (e: Exception) {
            Log.e("xoxo", "date conversion excepption:" + e.toString())
        }
        return ""
    }
    
    private fun showSnackBar(message: String, bool: Boolean) {
        Snackbar.make(view!!, message, Snackbar.LENGTH_LONG).show()
    }


    fun shareFacebook() {

        if (ShareDialog.canShow(ShareLinkContent::class.java)) {

            val shareLinkContent = ShareLinkContent.Builder()
                .setContentUrl(Uri.parse(Constants.FACEBOOK_URL + data.event_id))
                .build()
            shareDialog?.show(shareLinkContent)
        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager?.onActivityResult(requestCode, resultCode, data);
    }

    override fun onDetach() {

        eventDetailsViewModel.progressLiveData.postValue(false)
        super.onDetach()

    }
}



