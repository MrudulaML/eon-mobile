package `in`.bitspilani.eon.event_organiser.ui


import `in`.bitspilani.eon.R
import `in`.bitspilani.eon.event_organiser.ui.adapter.FilterPagerAdapter
import `in`.bitspilani.eon.event_organiser.viewmodel.EventDashboardViewModel
import `in`.bitspilani.eon.utils.px
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_filter_dialog.*


/**
 * A simple [Fragment] subclass.
 *
 */
class FilterDialogFragment() : DialogFragment(),
    FilterCallbackListener {

    private var eventTypeSelected: Int? = null
    private var subscriptionTypeSelected: String? = null
    private var eventStatusSelected: String? = null

    // tab titles
    private val titles =
        arrayOf("Type of Events", "Calender")

    private lateinit var eventDashboardViewModel: EventDashboardViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isCancelable = true
        return inflater.inflate(R.layout.fragment_filter_dialog, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //using common view fragment with activity scope for reuse
        //TODO use some better solution
        eventDashboardViewModel = activity?.run {
            ViewModelProviders.of(this).get(EventDashboardViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        setUpClickListeners()


    }
    //TODO so much hack ashutosh
    private fun setUpClickListeners() {

        val filterAdapter =FilterPagerAdapter(activity!!, this)
        val doppelgangerPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                when (position) {
                    1 -> {
                        val params: ViewGroup.LayoutParams = filter_view_pager.getLayoutParams()
                        params.height = 300.px
                        params.width = 300.px
                        filter_view_pager.setLayoutParams(params)
                        filterAdapter.notifyDataSetChanged()
                    }
                    0 -> {

                        val params: ViewGroup.LayoutParams = filter_view_pager.getLayoutParams()
                        params.height = 600.px
                        params.width = 300.px
                        filter_view_pager.setLayoutParams(params)
                        filterAdapter.notifyDataSetChanged()
                    }
                }
            }
        }

        filter_view_pager.adapter = filterAdapter

        TabLayoutMediator(filter_tab_layout, filter_view_pager) { tab, position ->
            tab.text = titles[position]
        }.attach()

        filter_view_pager.registerOnPageChangeCallback(doppelgangerPageChangeCallback)

    }

    override fun onApplyFilter(
        eventType: Int?,
        eventLocation: String?,
        startDate: String?,
        endDate: String?,
        fromFilter: Boolean,
        eventStatus: String?,
        subscriptionType: String?
    ) {
        eventDashboardViewModel.getEvents(
            eventTypeSelected,
            eventLocation,
            startDate,
            endDate,
            fromFilter,
            eventStatusSelected,
            subscriptionTypeSelected
        )
        dismiss()
    }

    override fun onFilterTypeSelected(eventType: Int?, eventStatu: String?, subType: String?) {

        eventType?.let {
            eventTypeSelected = it
        }

        eventStatu?.let {
            eventStatusSelected = it
        }

        subType?.let {
            subscriptionTypeSelected = it

        }

    }


}

interface FilterCallbackListener {
    fun onApplyFilter(
        eventType: Int? = null,
        eventLocation: String? = null,
        startDate: String? = null,
        endDate: String? = null,
        fromFilter: Boolean = false,
        eventStatus: String? = null,
        subscriptionType: String? = null

    )

    fun onFilterTypeSelected(
        eventType: Int? = null,
        eventStatus: String? = null,
        subType: String? = null
    )
}
