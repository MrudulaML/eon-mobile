package `in`.bitspilani.eon.event_organiser.ui

import `in`.bitspilani.eon.BitsEonActivity
import `in`.bitspilani.eon.R
import `in`.bitspilani.eon.event_organiser.models.EventType
import `in`.bitspilani.eon.event_organiser.models.FilterResponse
import `in`.bitspilani.eon.event_organiser.viewmodel.EventFilterViewModel
import `in`.bitspilani.eon.utils.Constants
import `in`.bitspilani.eon.utils.ModelPreferencesManager
import `in`.bitspilani.eon.utils.clickWithDebounce
import `in`.bitspilani.eon.utils.getViewModelFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.chip.Chip
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

        val eventTypeCached = ModelPreferencesManager.get<FilterResponse>(Constants.EVENT_TYPES)
        if(eventTypeCached!=null)
            populateFilters(eventTypeCached.data)
        else
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
            val radioButton = layoutInflater.inflate(R.layout.item_radio_button, radio_group_filter, false) as Chip
            radioButton.id = i.id
            radioButton.text = i.type
            radio_group_filter.addView(radioButton)
            radioButton.setOnClickListener {

                filterType = it.id

            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.e("Filter event fragment onDestroy")
    }


}