package `in`.bitspilani.eon.eventOrganiser.ui


import `in`.bitspilani.eon.BitsEonApp
import `in`.bitspilani.eon.R
import `in`.bitspilani.eon.eventOrganiser.data.IndividualEvent
import `in`.bitspilani.eon.eventOrganiser.ui.adapter.EventAdapter
import `in`.bitspilani.eon.login.ui.ActionbarHost
import `in`.bitspilani.eon.utils.*
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_dashboard.*


/**
 * A simple [Fragment] subclass.
 *
 */
class HomeFragment : Fragment(), CallbackListener {
    private val dashboardViewModel by viewModels<EventDashboardViewModel> { getViewModelFactory() }
    private var actionbarHost: ActionbarHost? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_dashboard, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //dummy recycler view
        initView()

        btn_filter.clickWithDebounce { showDialog() }

        setUpObservables()
    }

    private fun showDialog() {
        val dialogFragment = FilterDialogFragment(this)
        dialogFragment.show(childFragmentManager, "filterDialog")
    }

    private fun setUpObservables() {

        dashboardViewModel.eventDetails.observe(viewLifecycleOwner, Observer {

            if (ModelPreferencesManager.getInt(Constants.USER_ROLE)==UserType.SUBSCRIBER.ordinal)
                findNavController().navigate(R.id.action_homeFragment_to_eventDetailsFragment,
                    null,
                    NavOptions.Builder()
                        .setPopUpTo(R.id.homeFragment,
                            false).build())
            else
                findNavController().navigate(R.id.eventDetails)

        })

    }


    override fun onResume() {
        super.onResume()

        if(ModelPreferencesManager.getInt(Constants.USER_ROLE)==UserType.SUBSCRIBER.ordinal)
            actionbarHost?.showToolbar(showToolbar = true,title = "Event Management",showBottomNav = true)
        else
            actionbarHost?.showToolbar(showToolbar = true,title = "Event Management",showBottomNav = false)
    }

    private fun initView() {
        //show navigation
        //dummy list
        val listOfEvent = mutableListOf<IndividualEvent>()
        listOfEvent.add(
            IndividualEvent(
                "Food Festival", 1,
                "2000 Attendees"
            )
        )
        listOfEvent.add(
            IndividualEvent(
                "Music Festival", 2,
                "1000 Attendees"
            )
        )
        listOfEvent.add(
            IndividualEvent(
                "Technical Corridor", 3,
                "3000 Attendees"
            )
        )
        listOfEvent.add(
            IndividualEvent(
                "Financial Planning", 4,
                "4000 Attendees"
            )
        )
        listOfEvent.add(
            IndividualEvent(
                "Health and Fitness", 5,
                "3000 Attendees"
            )
        )
        listOfEvent.add(
            IndividualEvent(
                "Ethical Hacking", 6,
                "2500 Attendees"
            )
        )
        listOfEvent.add(
            IndividualEvent(
                "Angular JS Classes", 7,
                "2600 Attendees"
            )
        )

        rv_event_list.layoutManager = LinearLayoutManager(activity)
        rv_event_list.addItemDecoration(
            MarginItemDecoration(
                resources.getDimension(R.dimen._16sdp).toInt()
            )
        )
        val movieListAdapter =
            EventAdapter(
                listOfEvent,
                dashboardViewModel
            )
        rv_event_list.adapter = movieListAdapter

    }

    /**
     * toggle visibility of different navigation
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ActionbarHost) {
            actionbarHost = context
        }
    }



    /**
     * handle filter data here
     */
    override fun onDataReceived(data: String) {

    }


}
