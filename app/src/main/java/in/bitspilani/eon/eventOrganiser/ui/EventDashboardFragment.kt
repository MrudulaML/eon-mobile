package `in`.bitspilani.eon.eventOrganiser.ui


import `in`.bitspilani.eon.R
import `in`.bitspilani.eon.eventOrganiser.ui.adapter.EventAdapter
import `in`.bitspilani.eon.eventOrganiser.viewmodel.EventDetailsViewModel
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
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_dashboard.*
import timber.log.Timber


/**
 * A simple [Fragment] subclass.
 *
 */
class HomeFragment : Fragment(), CallbackListener {
    private val dashboardViewModel by viewModels<EventDetailsViewModel> { getViewModelFactory() }
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
        dashboardViewModel.getEvents()

        btn_filter.clickWithDebounce { showDialog() }

        setUpObservables()
    }

    private fun showDialog() {
        val dialogFragment = FilterDialogFragment(this)
        dialogFragment.show(childFragmentManager, "filterDialog")
    }

    private fun setUpObservables() {

        dashboardViewModel.eventDetails.observe(viewLifecycleOwner, Observer {


            Timber.e(it[0].name)
            rv_event_list.layoutManager = LinearLayoutManager(activity)
            rv_event_list.addItemDecoration(
                MarginItemDecoration(
                    resources.getDimension(R.dimen._16sdp).toInt()
                ))
            val movieListAdapter =
                EventAdapter(it, dashboardViewModel )
            rv_event_list.adapter = movieListAdapter

        })

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



    /**
     * handle filter data here
     */
    override fun onDataReceived(data: String) {

    }


}
