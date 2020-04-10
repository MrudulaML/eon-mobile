package `in`.bitspilani.eon.event_organiser.ui


import `in`.bitspilani.eon.R
import `in`.bitspilani.eon.event_organiser.ui.adapter.FilterPagerAdapter
import `in`.bitspilani.eon.event_organiser.viewmodel.EventDashboardViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_filter_dialog.*


/**
 * A simple [Fragment] subclass.
 *
 */
class FilterDialogFragment(private val eventDashboardViewModel: EventDashboardViewModel) : DialogFragment(),
    FilterCallbackListener {

    // tab titles
    private val titles =
        arrayOf("Type of Events", "Calender")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isCancelable = true
        return inflater.inflate(R.layout.fragment_filter_dialog, container, false)
    }

    override fun getTheme(): Int {
        return R.style.DialogTheme
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        filter_view_pager.adapter =
            FilterPagerAdapter(
                activity!!,this
            )
        TabLayoutMediator(filter_tab_layout, filter_view_pager) { tab, position ->
            //To get the first name of doppelganger celebrities
            tab.text = titles[position]
        }.attach()

    }

    override fun onApplyFilter(
        eventType: Int?,
        eventLocation: String?,
        startDate: String?,
        endDate: String?,
        fromFilter:Boolean
    ) {
        eventDashboardViewModel.getEvents(eventType, eventLocation, startDate, endDate,fromFilter)
        dismiss()
    }


}



interface FilterCallbackListener {
    fun onApplyFilter(
        eventType: Int? = null,
        eventLocation: String? = null,
        startDate: String? = null,
        endDate: String? = null,
        fromFilter:Boolean = false
    )
}
