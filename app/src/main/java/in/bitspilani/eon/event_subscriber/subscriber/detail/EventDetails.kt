package `in`.bitspilani.eon.event_subscriber.subscriber.detail

import `in`.bitspilani.eon.R
import `in`.bitspilani.eon.databinding.EventDetailsFragmentBinding
import `in`.bitspilani.eon.event_subscriber.models.Data
import `in`.bitspilani.eon.login.ui.ActionbarHost
import `in`.bitspilani.eon.utils.EmailValidator
import `in`.bitspilani.eon.utils.clickWithDebounce
import `in`.bitspilani.eon.utils.getViewModelFactory
import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.*
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
import kotlinx.android.synthetic.main.event_details_fragment.*
import kotlinx.android.synthetic.main.layout_dialog_payment_success.view.*
import kotlinx.android.synthetic.main.layout_email_share_to_friend.view.*
import kotlinx.android.synthetic.main.layout_seat_booking.*
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Environment
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap


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

        eventDetailsFragmentBinding.viewmodel = eventDetailsViewModel
        setObservables()

        eventDetailsViewModel.getEventDetails(4)

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
        // button download
        btn_download.clickWithDebounce {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ){
                if (activity?.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                    requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),STORAGE_PERMISSION_CODE)
                }else{
                    createPdf()
                }
            }else{

            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            STORAGE_PERMISSION_CODE ->{
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    createPdf()
                }else{
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

    // create pdf and save external directory
    private fun createPdf(){

        val eventName = tv_event_name.text.toString()
        val eventDateTime = tv_event_date.text.toString()
        val eventLocation = tv_event_location.text.toString()
        val eventSeatCounter = tv_seat_counter.text.toString()
        val eventAmount = btn_price.text.toString()

        val subscriberName = "Jhon@hashedin.com" // to do
        val bookingNotes = "It's non-transferable ticket" // to do
        val bookingDate = "Wednesday 14th April 20" // to do

        val logoBitmap = BitmapFactory.decodeResource(resources,R.drawable.logo_bits)
        val qrCodeBitmap = BitmapFactory.decodeResource(resources,R.drawable.ic_qr_code)

        // resize logo
        val resizedLogoBitmap = Bitmap.createScaledBitmap(
            logoBitmap, 128, 128, false
        )
        // resize QR code
        val resizedQRCodeBitmap = Bitmap.createScaledBitmap(
            qrCodeBitmap, 220, 220, false
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
        canvas.drawBitmap(resizedLogoBitmap, 40F,80F, bitmapPaint)

        // header
        titlePaint.textSize = resources.getDimension(R.dimen._24fs)
        titlePaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText(eventName,220F, 180F, titlePaint )

        // divider
        canvas.drawLine(40F, 248F, 1200-40F, 248F, linePaint)

        // dummy QR Code
        canvas.drawBitmap(resizedQRCodeBitmap, 40F,290F, bitmapPaint)

        // Event info
        textPaint.textSize = resources.getDimension(R.dimen._16fs)
        textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        canvas.drawText("Event Name: "+eventName, 300F, 350F, textPaint);
        canvas.drawText("Number of seats: "+eventSeatCounter, 300F, 410F, textPaint);
        canvas.drawText("Amount: "+eventAmount, 300F, 470F, textPaint);
        canvas.drawText("Event Date: "+eventDateTime, 300F, 530F, textPaint);
        canvas.drawText("Location: "+eventLocation, 300F, 590F, textPaint);
        canvas.drawText("Subscriber Name: "+subscriberName, 300F, 650F, textPaint);
        canvas.drawText("Booking Date: "+bookingDate, 300F, 710F, textPaint);

        // Note
        textPaint.textSize = resources.getDimension(R.dimen._16fs)
        textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("*Note: "+bookingNotes, 40F, 800F, textPaint);

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
            Log.e("Invoice","Tickets"+filePath)
        }catch (e : IOException) {
            Log.e("Invoice", "Error: "+e.toString());
            showUserMsg("Error!")
        }

        document.writeTo(FileOutputStream(filePath));
        document.close();
    }

    fun toSimpleString(date: Date) : String {
        val format = SimpleDateFormat("dd/MM/yyy")
        return format.format(date)
    }
}
