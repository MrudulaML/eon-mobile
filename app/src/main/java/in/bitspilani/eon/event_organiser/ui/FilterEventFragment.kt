package `in`.bitspilani.eon.event_organiser.ui

import `in`.bitspilani.eon.BitsEonActivity
import `in`.bitspilani.eon.R
import `in`.bitspilani.eon.event_organiser.models.EventType
import `in`.bitspilani.eon.event_organiser.viewmodel.EventFilterViewModel
import `in`.bitspilani.eon.utils.clickWithDebounce
import `in`.bitspilani.eon.utils.getViewModelFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_filter_event.*
import timber.log.Timber


class FilterEventFragment(
    private val filterCallbackListener: FilterCallbackListener
) : Fragment() {

    private val eventFilterViewModel by viewModels<EventFilterViewModel> { getViewModelFactory() }
    private var filterType: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_filter_event, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        eventFilterViewModel.getFilter()

        setObservables()
        setUpClickListeners()


    }

    private fun setUpClickListeners() {

        btn_filter.clickWithDebounce {

            filterCallbackListener.onApplyFilter(eventType = filterType, fromFilter = true)
        }


    }

    private fun setObservables() {
        eventFilterViewModel.eventFilterObservable.observe(viewLifecycleOwner, Observer {
            populateFilters(it)
        })
        eventFilterViewModel.progressLiveData.observe(viewLifecycleOwner, Observer {

            (activity as BitsEonActivity).showProgress(it)
        })

    }

    private fun populateFilters(eventTypeList: List<EventType>) {

        for (i in eventTypeList) {
            val rbn = RadioButton(activity)
            rbn.id = i.id
            rbn.text = i.type
            radio_group_filter.addView(rbn)
            rbn.setOnClickListener {

                filterType = it.id
                Toast.makeText(activity, "" + filterType, Toast.LENGTH_SHORT)
                    .show()
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.e("Filter event fragment onDestroy")
    }


}