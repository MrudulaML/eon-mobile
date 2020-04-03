package `in`.bitspilani.eon.presentation.event.fragments


import `in`.bitspilani.eon.R
import `in`.bitspilani.eon.data.restservice.models.IndividualEvent
import `in`.bitspilani.eon.presentation.BitsEonActivity
import `in`.bitspilani.eon.presentation.event.adapter.EventAdapter
import `in`.bitspilani.eon.utils.GridSpacingItemDecoration
import `in`.bitspilani.eon.utils.getViewModelFactory
import `in`.bitspilani.eon.viewmodel.HomeViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.fragment_home.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class HomeFragment : Fragment() {
    private val homeViewModel by viewModels<HomeViewModel> { getViewModelFactory() }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        (activity as AppCompatActivity?)!!.supportActionBar?.hide()
        return inflater.inflate(R.layout.fragment_home, container, false)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as BitsEonActivity).binding.toolbar.visibility = View.VISIBLE
        initView()
    }


    private fun initView() {
        //dummy list
        val listOfEvent = mutableListOf<IndividualEvent>()
        listOfEvent.add(IndividualEvent("Food Festival","2000"))
        listOfEvent.add(IndividualEvent("Music Festival","1000"))
        listOfEvent.add(IndividualEvent("Technical Corridor","3000"))
        listOfEvent.add(IndividualEvent("Financial Planning","4000"))
        listOfEvent.add(IndividualEvent("Health and Fitness","3000"))
        listOfEvent.add(IndividualEvent("Ethical Hacking","2500"))
        listOfEvent.add(IndividualEvent("Angular JS Classes","2600"))

        rv_event_list.layoutManager = GridLayoutManager(activity,2)

        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.grid_layout_margin)
        rv_event_list.addItemDecoration(GridSpacingItemDecoration(2, spacingInPixels, true, 0))

        val movieListAdapter = EventAdapter(listOfEvent,homeViewModel)
        rv_event_list.adapter = movieListAdapter

    }


}
