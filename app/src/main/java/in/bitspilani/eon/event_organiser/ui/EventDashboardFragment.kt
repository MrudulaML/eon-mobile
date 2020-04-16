package `in`.bitspilani.eon.event_organiser.ui


import `in`.bitspilani.eon.BitsEonActivity
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
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_dashboard.*
import timber.log.Timber


/**
 * A simple [Fragment] subclass.
 *
 */
class HomeFragment : Fragment() {
   // private val dashboardViewModel by viewModels<EventDashboardViewModel> { getViewModelFactory() }
    private var actionbarHost: ActionbarHost? = null
    private var isWishListed : Boolean =false
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var eventAdapter: EventAdapter
    private lateinit var eventDashboardViewModel: EventDashboardViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        eventDashboardViewModel = activity?.run {
            ViewModelProviders.of(this).get(EventDashboardViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        eventDashboardViewModel.getEvents()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_dashboard, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpObservables()
        setUpClickListeners()
        setUpSearch()

    }


    private fun setUpSearch() {
        event_search_view.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                defocusAndHideKeyboard(activity)
                return false
            }


            override fun onQueryTextChange(newText: String?): Boolean {

                if(newText.isNullOrEmpty()){
                    eventDashboardViewModel.getEvents()
                    defocusAndHideKeyboard(activity)
                }
                else
                    eventAdapter.filter(newText.toString())

                return false
            }


        })
    }

    private fun setUpClickListeners() {
        btn_filter.clickWithDebounce {
            //TODO move it to nav graph please
            val dialogFragment = FilterDialogFragment()
            dialogFragment.show(childFragmentManager, "filterDialog")

        }
        btn_filter_clear.clickWithDebounce {
            btn_filter_clear.visibility=View.GONE
            eventDashboardViewModel.getEvents()
        }
    }

    private fun setUpObservables() {
        eventDashboardViewModel.eventDetailsObservables.observe(viewLifecycleOwner, Observer {
            setEventRecyclerView(it)
        })

        eventDashboardViewModel.eventClickObservable.observe(viewLifecycleOwner, Observer {
            if (ModelPreferencesManager.getInt(Constants.USER_ROLE)==1)
                findNavController().navigate(R.id.action_homeFragment_to_organiser_eventDetailsFragment,
                    bundleOf("id" to it),
                    NavOptions.Builder()
                        .setPopUpTo(R.id.homeFragment,
                            false).build())
            else
                //TODO change this to builder pattern
                findNavController().navigate(
                    R.id.eventDetails,
                    bundleOf(Constants.EVENT_ID to it)
                )
        })

        eventDashboardViewModel.progressLiveData.observe(viewLifecycleOwner, Observer {

            (activity as BitsEonActivity).showProgress(it)
        })

    }

    private fun setEventRecyclerView(eventList: EventList) {

        val isSubscriber :  Boolean = ModelPreferencesManager.getInt(Constants.USER_ROLE)==2
        Timber.e("is subcriber$isSubscriber")
        layoutManager = LinearLayoutManager(activity)
        //TODO fix this hack
        if (eventList.fromFilter) {
            btn_filter_clear.visibility=View.VISIBLE
            bindList(eventList, isSubscriber)
        }
        else
            bindList(eventList, isSubscriber)

    }

    private fun bindList(
        eventList: EventList,
        isSubscriber: Boolean = false
    ) {
        rv_event_list.layoutManager = layoutManager
        eventAdapter = EventAdapter(eventList.eventList, eventDashboardViewModel, isSubscriber)
        rv_event_list.adapter = eventAdapter
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
