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
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.dialog_success_reminder.view.*
import kotlinx.android.synthetic.main.fragment_event.*
import timber.log.Timber


/**
 * A simple [Fragment] subclass.
 *
 */
class PagerEventFragment(private val eventDetailResponse: DetailResponseOrganiser) : Fragment(),
    NotifySubscriberCallback {
    private var isFromUpdate = true
    private val eventDetailOrganiserViewModel by viewModels<EventDetailOrganiserViewModel> { getViewModelFactory() }

    lateinit var binding: FragmentEventBinding
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

    }

    private fun setOffEventsForOrganiser() {

        linearLayout2.goneUnless(eventDetailResponse.data.self_organised)
        linearLayout3.goneUnless(eventDetailResponse.data.self_organised)
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
            view?.showSnackbar(it, 0)}

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
            isFromUpdate=false
            val dialogFragment = NotifySubscriberFragment(this,isFromUpdate)
            dialogFragment.show(childFragmentManager, "AaddInviteeDialog")
        }
        send_reminder.clickWithDebounce {
            isFromUpdate=true
            val dialogFragment = NotifySubscriberFragment(this,isFromUpdate)
            dialogFragment.show(childFragmentManager, "AaddInviteeDialog")
        }
    }

    override fun onUpdate(message: String) {
        Timber.e("on notify confirm")
        eventDetailOrganiserViewModel.notifySubscriber(
            type = "updates",
            event_id =  eventDetailResponse.data.id,
            message = message
        )
    }

    override fun onReminder(message: String) {
        Timber.e("reminder")
        isFromUpdate = false
        eventDetailOrganiserViewModel.notifySubscriber(
            type = "reminder",
            event_id =  eventDetailResponse.data.id,
            message = message
        )
    }
}