package `in`.bitspilani.eon.event.ui


import `in`.bitspilani.eon.BitsEonActivity
import `in`.bitspilani.eon.R
import `in`.bitspilani.eon.event.data.IndividualEvent
import `in`.bitspilani.eon.utils.GridSpacingItemDecoration
import `in`.bitspilani.eon.utils.getViewModelFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_dashboard.*

/**
 * A simple [Fragment] subclass.
 *
 */
class HomeFragment : Fragment() {
    private val homeViewModel by viewModels<EventDashboardViewModel> { getViewModelFactory() }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }


    private fun initView() {
        //dummy list
        val listOfEvent = mutableListOf<IndividualEvent>()
        listOfEvent.add(
            IndividualEvent(
                "Food Festival",
                "2000"
            )
        )
        listOfEvent.add(
            IndividualEvent(
                "Music Festival",
                "1000"
            )
        )
        listOfEvent.add(
            IndividualEvent(
                "Technical Corridor",
                "3000"
            )
        )
        listOfEvent.add(
            IndividualEvent(
                "Financial Planning",
                "4000"
            )
        )
        listOfEvent.add(
            IndividualEvent(
                "Health and Fitness",
                "3000"
            )
        )
        listOfEvent.add(
            IndividualEvent(
                "Ethical Hacking",
                "2500"
            )
        )
        listOfEvent.add(
            IndividualEvent(
                "Angular JS Classes",
                "2600"
            )
        )

        rv_event_list.layoutManager = LinearLayoutManager(activity)
        val movieListAdapter = EventAdapter(
                listOfEvent,
                homeViewModel
            )
        rv_event_list.adapter = movieListAdapter

    }


}
