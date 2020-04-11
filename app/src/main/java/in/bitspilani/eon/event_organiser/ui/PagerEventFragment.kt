package `in`.bitspilani.eon.event_organiser.ui




import `in`.bitspilani.eon.R
import `in`.bitspilani.eon.databinding.FragmentBasicDetailsBinding
import `in`.bitspilani.eon.databinding.FragmentEventBinding
import `in`.bitspilani.eon.event_organiser.models.DetailResponseOrganiser
import `in`.bitspilani.eon.event_subscriber.models.EventDetailResponse
import `in`.bitspilani.eon.event_subscriber.subscriber.detail.EventDetailsViewModel
import `in`.bitspilani.eon.utils.clickWithDebounce
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_event.*
import timber.log.Timber


/**
 * A simple [Fragment] subclass.
 *
 */
class PagerEventFragment(private val eventDetailResponse: DetailResponseOrganiser) : Fragment() {

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
        binding.eventDetail=eventDetailResponse
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpClickListeners()
    }



    private fun setUpClickListeners() {
        share_fb.clickWithDebounce {

            invokeShare(activity!!,"","")
        }
        send_reminder.clickWithDebounce { showUserMsg("Send reminders successfully") }
        send_updates.clickWithDebounce { showUserMsg("Send updates successfully") }
    }

    private fun showUserMsg(msg:String){
        Toast.makeText(activity,msg, Toast.LENGTH_LONG).show()
    }

    private fun invokeShare(
        activity: Activity,
        quote: String?,
        credit: String?
    ) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(
            Intent.EXTRA_SUBJECT,
            "Example"
        )
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Example text")
        activity.startActivity(
            Intent.createChooser(
                shareIntent,
                "Example"
            )
        )
    }
}
