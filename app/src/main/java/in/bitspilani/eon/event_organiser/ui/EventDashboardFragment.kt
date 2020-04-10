package `in`.bitspilani.eon.event_organiser.ui


import `in`.bitspilani.eon.R
import `in`.bitspilani.eon.event_organiser.models.EventList
import `in`.bitspilani.eon.event_organiser.ui.adapter.EventAdapter
import `in`.bitspilani.eon.event_organiser.viewmodel.EventDashboardViewModel
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
class HomeFragment : Fragment() {
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

        dashboardViewModel.getEvents()


        setUpObservables()
        setUpClickListeners()
    }

    private fun setUpClickListeners() {
        btn_filter.clickWithDebounce {
            val dialogFragment = FilterDialogFragment(dashboardViewModel)
            dialogFragment.show(childFragmentManager, "filterDialog")
        }
    }

    private fun setUpObservables() {

        dashboardViewModel.eventDetailsObservables.observe(viewLifecycleOwner, Observer {

            setEventRecyclerView(it)
        })

        dashboardViewModel.eventClickObservable.observe(viewLifecycleOwner, Observer {

            if (ModelPreferencesManager.getInt(Constants.USER_ROLE)==1)
                findNavController().navigate(R.id.action_homeFragment_to_eventDetailsFragment,
                    null,
                    NavOptions.Builder()
                        .setPopUpTo(R.id.homeFragment,
                            false).build())
            else
                findNavController().navigate(R.id.eventDetails)
        })

    }
    private lateinit var eventListAdapter : EventAdapter
    private fun setEventRecyclerView(eventList: EventList) {

        if (eventList.fromFilter) {
            rv_event_list.invalidateItemDecorations()
            rv_event_list.invalidate()
            eventListAdapter = EventAdapter(eventList.eventList, dashboardViewModel)
            rv_event_list.adapter = eventListAdapter

        }
        else {

            rv_event_list.layoutManager = LinearLayoutManager(activity)
            rv_event_list.addItemDecoration(
                MarginItemDecoration(
                    resources.getDimension(R.dimen._16sdp).toInt()
                )
            )
            eventListAdapter = EventAdapter(eventList.eventList, dashboardViewModel)
            rv_event_list.adapter = eventListAdapter
        }
    }


    override fun onResume() {
        super.onResume()

        if(ModelPreferencesManager.getInt(Constants.USER_ROLE)==UserType.SUBSCRIBER.ordinal)
            actionbarHost?.showToolbar(showToolbar = true,title = "Event Management",showBottomNav = true)
        else
            actionbarHost?.showToolbar(showToolbar = true,title = "Event Management",showBottomNav = false)
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
}
