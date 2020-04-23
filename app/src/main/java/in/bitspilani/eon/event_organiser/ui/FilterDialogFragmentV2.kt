package `in`.bitspilani.eon.event_organiser.ui


import `in`.bitspilani.eon.BitsEonActivity
import `in`.bitspilani.eon.R
import `in`.bitspilani.eon.event_organiser.models.EventType
import `in`.bitspilani.eon.event_organiser.ui.adapter.FilterPagerAdapter
import `in`.bitspilani.eon.event_organiser.viewmodel.EventDashboardViewModel
import `in`.bitspilani.eon.event_organiser.viewmodel.EventFilterViewModel
import `in`.bitspilani.eon.utils.getViewModelFactory
import `in`.bitspilani.eon.utils.px
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_filter_dialog.*
import kotlinx.android.synthetic.main.fragment_filter_revamped.*


/**
 * A simple [Fragment] subclass.
 *
 */
class FilterDialogFragmentV2() : DialogFragment() {

    private val eventFilterViewModel by viewModels<EventFilterViewModel> { getViewModelFactory() }
    private var eventList: ArrayList<String> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isCancelable = true
        return inflater.inflate(R.layout.fragment_filter_revamped, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        eventFilterViewModel.getFilter()


        eventFilterViewModel.eventFilterObservable.observe(viewLifecycleOwner, Observer {
            populateFilters(it)
        })
        eventFilterViewModel.progressLiveData.observe(viewLifecycleOwner, Observer {

            (activity as BitsEonActivity).showProgress(it)
        })
    }

    private fun populateFilters(eventTypes: List<EventType>) {

        for (i in eventTypes) {
            eventList.add(i.type)
        }

        val adapter = NoPaddingArrayAdapter(
            context!!,
            R.layout.spinner_item_row, eventList
        )
        event_types_spinner.adapter = adapter

        //type
        val adapter2 = NoPaddingArrayAdapter(
            context!!,
            R.layout.spinner_item_row, arrayListOf("Comppleted","Upcming","Canclle")
        )
        event_status_spinner.adapter = adapter2

        //type
        val adapter3 = NoPaddingArrayAdapter(
            context!!,
            R.layout.spinner_item_row, arrayListOf("Free","Paid")
        )
        event_fees_spinner.adapter = adapter3

    }


}

open class NoPaddingArrayAdapter<T>(context: Context, layoutId: Int, items: List<T>?) : ArrayAdapter<T>(context, layoutId,
    items as MutableList<T>
) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)
        view.setPadding(0,view.paddingTop,view.paddingRight,view.paddingBottom)
        return view
    }
}


