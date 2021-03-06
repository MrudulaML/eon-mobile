package `in`.bitspilani.eon.event_organiser.ui



import `in`.bitspilani.eon.BitsEonActivity
import `in`.bitspilani.eon.R
import `in`.bitspilani.eon.event_organiser.ui.adapter.EventDetailPagerAdapter
import `in`.bitspilani.eon.event_organiser.viewmodel.EventDetailOrganiserViewModel
import `in`.bitspilani.eon.login.ui.ActionbarHost
import `in`.bitspilani.eon.utils.getViewModelFactory
import `in`.bitspilani.eon.utils.goneUnless
import `in`.bitspilani.eon.utils.showSnackbar
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_event_detail_organiser.*
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*


/**
 * A simple [Fragment] subclass.
 *
 */
class EventDetailsOrganiserFragment : Fragment(),InviteeCallbackListener,EventDetailsCallbackListener {

    // tab titles
    private val titles =
        arrayOf("Events", "Invitees")

    private val eventDetailOrganiserViewModel by viewModels<EventDetailOrganiserViewModel> { getViewModelFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity!!.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_event_detail_organiser, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //setup pager
        Timber.e("eventid ${arguments?.getInt("id")}")
        arguments?.getInt("id")?.let {
            eventDetailOrganiserViewModel.getEventDetails(it)
        }


        setUpClickListeners()
        setUpObservables()

    }



    private fun setUpObservables() {

        eventDetailOrganiserViewModel.eventData.observe(viewLifecycleOwner, Observer {

            if(!it.data.self_organised ) tab_layout.visibility=View.GONE
            event_detail_view_pager.adapter=
                EventDetailPagerAdapter(
                    activity!!,it,this
                )
            TabLayoutMediator(tab_layout, event_detail_view_pager) { tab, position ->
                //To get the first name of doppelganger celebrities
                tab.text = titles[position]
            }.attach()
        })

        eventDetailOrganiserViewModel.progressLiveData.observe(viewLifecycleOwner, Observer {

            (activity as BitsEonActivity).showProgress(it)
        })

        eventDetailOrganiserViewModel.errorView.observe(viewLifecycleOwner, Observer {

            view?.showSnackbar(it,0)
        })


    }


    private fun setUpClickListeners() {

        toolbar_fragment.setNavigationOnClickListener {

            findNavController().popBackStack()
        }
    }


    /**
     * toggle visibility of different navigation
     */
    private var actionbarHost: ActionbarHost? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ActionbarHost) {
            actionbarHost = context
        }
    }

    override fun onResume() {
        super.onResume()
        actionbarHost?.showToolbar(showToolbar = false,showBottomNav = false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        setHasOptionsMenu(false)
        (activity as AppCompatActivity?)!!.supportActionBar!!.setDisplayHomeAsUpEnabled(false)
    }

    override fun getDelete(showDelete: Boolean) {
        toolbar_fragment.goneUnless(!showDelete)
        tab_layout.goneUnless(!showDelete)
    }

    override fun getEvent() {
        arguments?.getInt("id")?.let {

            eventDetailOrganiserViewModel.getEventDetails(id)
        }
    }


}

interface EventDetailsCallbackListener {
    fun getEvent()
}
interface InviteeCallbackListener {
    fun getDelete(showDelete: Boolean)
}


