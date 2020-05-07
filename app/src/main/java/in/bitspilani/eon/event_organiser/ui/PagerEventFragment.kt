package `in`.bitspilani.eon.event_organiser.ui


import `in`.bitspilani.eon.BitsEonActivity
import `in`.bitspilani.eon.R
import `in`.bitspilani.eon.databinding.FragmentEventBinding
import `in`.bitspilani.eon.event_organiser.models.DetailResponseOrganiser
import `in`.bitspilani.eon.event_organiser.viewmodel.EventDetailOrganiserViewModel
import `in`.bitspilani.eon.utils.*
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.facebook.CallbackManager
import com.facebook.share.model.ShareLinkContent
import com.facebook.share.widget.ShareDialog
import kotlinx.android.synthetic.main.dialog_success_reminder.view.*
import kotlinx.android.synthetic.main.fragment_event.*
import timber.log.Timber
import java.util.*


/**
 * A simple [Fragment] subclass.
 *
 */
class PagerEventFragment(private val eventDetailResponse: DetailResponseOrganiser) : Fragment(),
    NotifySubscriberCallback {
    private var isFromUpdate = true
    private val eventDetailOrganiserViewModel by viewModels<EventDetailOrganiserViewModel> { getViewModelFactory() }

    lateinit var binding: FragmentEventBinding
    var shareDialog: ShareDialog? = null
    var callbackManager: CallbackManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as AppCompatActivity?)!!.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity?)!!.supportActionBar!!.setHomeButtonEnabled(true)
    }

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_event, container, false)
        binding.eventDetail = eventDetailResponse
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpClickListeners()
        setUpObservables()
        setOffEventsForOrganiser()
        callbackManager = CallbackManager.Factory.create()
        shareDialog = ShareDialog(this)

        ll_viewfeedback.clickWithDebounce {


            if (eventDetailResponse.data.self_organised) {

                findNavController().navigate(
                    R.id.action_PagerEvent_to_feedback,
                    bundleOf(
                        Constants.EVENT_ID to eventDetailResponse.data.id,
                        Constants.FEEDBACK_COUNT to eventDetailResponse.data.feedback_count
                    )
                )
            } else {

                showUserMsg("You can only see feedbacks of events organized by you")

            }

        }

    }

    fun showUserMsg(msg: String) {


        Toast.makeText(activity!!, msg, Toast.LENGTH_LONG).show()
    }

    //TODO fix this with data binding
    private fun setOffEventsForOrganiser() {

        if (eventDetailResponse.data.self_organised) {

            ll_first_row.goneUnless(true)
            ll_second_row.goneUnless(true)
            ll_third_row.goneUnless(true)
            when (eventDetailResponse.data.event_status.toLowerCase(Locale.ROOT)) {
                "completed" -> {
                    enableNotify(false)
                }
                "cancelled" -> {

                    enableNotify(false)

                }

            }

        }

    }

    private fun enableNotify(show: Boolean) {
        send_updates.isEnabled = show
        send_reminder.isEnabled = show
    }

    private fun setUpObservables() {
        eventDetailOrganiserViewModel.notifyLiveData.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (!isFromUpdate) {
                    isFromUpdate = true
                    openReminderDialog(it.message)
                } else
                    view?.showSnackbar(it.message, 0)
            }
        })
        eventDetailOrganiserViewModel.progressLiveData.observe(viewLifecycleOwner, Observer {

            (activity as BitsEonActivity).showProgress(it)
        })
        eventDetailOrganiserViewModel.errorView.observe(viewLifecycleOwner, Observer {
            it?.let {
                view?.showSnackbar(it, 0)
            }

        })


    }

    //TODO fix this use appcompat
    private fun openReminderDialog(message: String) {
        val builder =
            AlertDialog.Builder(context!!)
        val layoutInflaterAndroid = LayoutInflater.from(activity)
        val view2: View =
            layoutInflaterAndroid.inflate(R.layout.dialog_success_reminder, null)
        builder.setView(view2)
        builder.setCancelable(true)
        val alertDialog = builder.create()
        alertDialog.show()

        view2.btn_send_reminder.clickWithDebounce {
            alertDialog.dismiss()
        }

    }

    private fun setUpClickListeners() {
        share_fb.clickWithDebounce {
            shareFacebook() // to do
        }

        //TODO handle thi elegently
        text_url.clickWithDebounce {
            try {
                val browserIntent =
                    Intent(Intent.ACTION_VIEW, Uri.parse(eventDetailResponse.data.external_links))
                startActivity(browserIntent)
            } catch (e: Exception) {

            }
        }
        send_updates.clickWithDebounce {
            isFromUpdate = false
            val dialogFragment = NotifySubscriberFragment(this, isFromUpdate)
            dialogFragment.show(childFragmentManager, "AaddInviteeDialog")
        }
        send_reminder.clickWithDebounce {
            isFromUpdate = true
            val dialogFragment = NotifySubscriberFragment(this, isFromUpdate)
            dialogFragment.show(childFragmentManager, "AaddInviteeDialog")
        }
    }

    override fun onUpdate(message: String) {
        Timber.e("on notify confirm")
        eventDetailOrganiserViewModel.notifySubscriber(
            type = "updates",
            event_id = eventDetailResponse.data.id,
            message = message
        )
    }

    override fun onReminder(message: String) {
        Timber.e("reminder")
        isFromUpdate = false
        eventDetailOrganiserViewModel.notifySubscriber(
            type = "reminder",
            event_id = eventDetailResponse.data.id,
            message = message
        )
    }

    fun shareFacebook() {

        if (ShareDialog.canShow(ShareLinkContent::class.java)) {

            val shareLinkContent = ShareLinkContent.Builder()
                .setContentUrl(Uri.parse(Constants.FACEBOOK_URL + eventDetailResponse.data.id))
                .build()
            shareDialog?.show(shareLinkContent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager?.onActivityResult(requestCode, resultCode, data);
    }
}
