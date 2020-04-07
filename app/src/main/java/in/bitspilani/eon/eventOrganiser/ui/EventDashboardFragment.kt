package `in`.bitspilani.eon.eventOrganiser.ui


import `in`.bitspilani.eon.R
import `in`.bitspilani.eon.eventOrganiser.data.IndividualEvent
import `in`.bitspilani.eon.utils.MarginItemDecoration
import `in`.bitspilani.eon.utils.clickWithDebounce
import `in`.bitspilani.eon.utils.getViewModelFactory
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_dashboard.*


/**
 * A simple [Fragment] subclass.
 *
 */
class HomeFragment : Fragment(), CallbackListener {
    private val dashboardViewModel by viewModels<EventDashboardViewModel> { getViewModelFactory() }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_dashboard, container, false)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()

        btn_filter.clickWithDebounce {

            showDialog()
        }

        setUpObservables()
    }

    private fun showDialog() {
        val dialogFragment = FilterDialogFragment(this)
        dialogFragment.show(childFragmentManager, "signature")
    }

    private fun setUpObservables() {

        dashboardViewModel.eventDetails.observe(viewLifecycleOwner, Observer {


            findNavController().navigate(R.id.eventDetailsFragment)


        })

    }


    override fun onResume() {
        super.onResume()
        activity?.title = "Event Management"
    }

    private fun initView() {
        //dummy list
        val listOfEvent = mutableListOf<IndividualEvent>()
        listOfEvent.add(
            IndividualEvent(
                "Food Festival", 1,
                "2000"
            )
        )
        listOfEvent.add(
            IndividualEvent(
                "Music Festival", 2,
                "1000"
            )
        )
        listOfEvent.add(
            IndividualEvent(
                "Technical Corridor", 3,
                "3000"
            )
        )
        listOfEvent.add(
            IndividualEvent(
                "Financial Planning", 4,
                "4000"
            )
        )
        listOfEvent.add(
            IndividualEvent(
                "Health and Fitness", 5,
                "3000"
            )
        )
        listOfEvent.add(
            IndividualEvent(
                "Ethical Hacking", 6,
                "2500"
            )
        )
        listOfEvent.add(
            IndividualEvent(
                "Angular JS Classes", 7,
                "2600"
            )
        )

        rv_event_list.layoutManager = LinearLayoutManager(activity)
        rv_event_list.addItemDecoration(
            MarginItemDecoration(
                resources.getDimension(R.dimen._16sdp).toInt()
            )
        )
        val movieListAdapter = EventAdapter(
            listOfEvent,
            dashboardViewModel
        )
        rv_event_list.adapter = movieListAdapter

    }

    override fun onDataReceived(data: String) {

    }


}
