package `in`.bitspilani.eon.eventOrganiser.ui

import `in`.bitspilani.eon.R
import `in`.bitspilani.eon.utils.clickWithDebounce
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_filter_calender.*
import timber.log.Timber

class FilterCalenderFragment(private val filterCallbackListener: FilterCallbackListener) : Fragment() {

    var startDate : String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_filter_calender, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        calender_view_filter.setOnDateChangeListener { cView, year, month, dayOfMonth ->

            startDate="$dayOfMonth-$month-$year"

        }

        btn_filter.clickWithDebounce {
            Timber.e("Filter data calender $startDate")
            filterCallbackListener.onApplyFilter(startDate =startDate,fromFilter = true )
        }
    }




}