package `in`.bitspilani.eon.event_organiser.ui


import `in`.bitspilani.eon.BitsEonActivity
import `in`.bitspilani.eon.R
import `in`.bitspilani.eon.event_organiser.models.EventType
import `in`.bitspilani.eon.event_organiser.models.FilterResponse
import `in`.bitspilani.eon.event_organiser.models.SelectedFilter
import `in`.bitspilani.eon.event_organiser.viewmodel.EventDashboardViewModel
import `in`.bitspilani.eon.utils.Constants
import `in`.bitspilani.eon.utils.ModelPreferencesManager
import `in`.bitspilani.eon.utils.clickWithDebounce
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_filter_revamped.*
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*


/**
 * A simple [Fragment] subclass.
 *
 */
class FilterDialogFragmentV2 : DialogFragment() {


    private var eventList: ArrayList<String> = arrayListOf()
    private lateinit var eventDashboardViewModel: EventDashboardViewModel

    private var selectedEventType: Int? = null
    private var selectedEventStatus: String? = null
    private var selectedEventFees: String? = null
    private var selectedEventStatusPos: Int? = null
    private var selectedEventFeesPos: Int? = null
    private var startDate: String? = null
    private var endDate: String? = null
    private var isCreatedByMe: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        eventDashboardViewModel = activity?.run {
            ViewModelProviders.of(this).get(EventDashboardViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

    }

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

        if(ModelPreferencesManager.getInt(Constants.USER_ROLE)==2)
            chb_by_me.visibility=View.GONE
        val user =  ModelPreferencesManager.get<FilterResponse>(Constants.EVENT_TYPES)
        user?.data?.let { populateFilters(it) }

        setDatePicker(edt_start_date)
        setDatePicker(edt_end_date)

        setUpObservables()
        setUpClickEvents()


    }

    private fun setUpClickEvents() {
        btn_reset.clickWithDebounce {
            resetFilters()
            eventDashboardViewModel.getEvents()
            dismiss()
        }

        btn_filter.clickWithDebounce {

            filterEvents()
        }
    }

    private fun setUpObservables() {
        eventDashboardViewModel.eventFilterObservable.observe(viewLifecycleOwner, Observer {

        })
        eventDashboardViewModel.progressLiveData.observe(viewLifecycleOwner, Observer {

            (activity as BitsEonActivity).showProgress(it)
        })
        eventDashboardViewModel.selectedFilterObservable.observe(viewLifecycleOwner, Observer {

            setPreSelectedFilters(it)
        })
    }

    private fun filterEvents() {
        if (!edt_start_date.text.isNullOrEmpty()) startDate = edt_start_date.text.toString()
        if (!edt_end_date.text.isNullOrEmpty()) endDate = edt_end_date.text.toString()

        eventDashboardViewModel.getEvents(
            eventType = selectedEventType,
            eventStatus = selectedEventStatus,
            startDate = startDate,
            endDate = endDate,
            subscriptionType = selectedEventFees,
            isByMe = isCreatedByMe
        )

        eventDashboardViewModel.setSelectedFilter(
            SelectedFilter(
                selectedEventType,
                selectedEventStatusPos,
                selectedEventFeesPos,
                startDate,
                endDate,
                isCreatedByMe
            )
        )

        Timber.e(
            "selected filters event type${selectedEventType} event status $selectedEventStatusPos event fees $selectedEventFeesPos start date $startDate end date $endDate is created by me $isCreatedByMe"
        )
        dismiss()
    }

    private fun setSpinners() {
        event_types_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                selectedEventType = if (position == 0)
                    null
                else
                    position
                Timber.e("Filter $selectedEventType")
            }

        }

        event_status_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedEventStatusPos=position
                selectedEventStatus =
                    if (parent?.getItemAtPosition(position).toString().toLowerCase()
                            .equals("upcoming")
                    )
                        null
                    else
                        parent?.getItemAtPosition(position).toString()

            }

        }

        event_fees_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedEventFeesPos=position
                selectedEventFees = if (position == 0)
                    null
                else
                    parent?.getItemAtPosition(position).toString()
                Timber.e("Filter $selectedEventFees")
            }

        }
        //get pre selected filter values
        eventDashboardViewModel.getSelectedFilters()
    }

    private fun resetFilters() {

        eventDashboardViewModel.resetFilters()
    }

    private fun setDatePicker(dateEditText: EditText) {

        val myCalendar = Calendar.getInstance()
        val datePickerOnDataSetListener =
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, monthOfYear)
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateLabel(myCalendar, dateEditText)
            }
        dateEditText.setOnClickListener {
            DatePickerDialog(
                activity!!, datePickerOnDataSetListener, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun updateLabel(myCalendar: Calendar, dateEditText: EditText) {
        val format = SimpleDateFormat("yyyy-MM-dd")
        dateEditText.setText(format.format(myCalendar.time))
    }

    private fun populateFilters(eventTypes: List<EventType>) {

        eventList.add("All")
        for (i in eventTypes) {
            eventList.add(i.type)
        }

        val eventTypeAdapter = ArrayAdapter(
            context!!,
            R.layout.spinner_item_row, eventList
        )
        event_types_spinner.adapter = eventTypeAdapter


        val eventStatusAdapter = ArrayAdapter(
            context!!,
            R.layout.spinner_item_row, arrayListOf("Upcoming", "Completed", "Cancelled","All")
        )
        event_status_spinner.adapter = eventStatusAdapter


        val eventFeesAdapter = ArrayAdapter(
            context!!,
            R.layout.spinner_item_row, arrayListOf("All", "Free", "Paid")
        )
        event_fees_spinner.adapter = eventFeesAdapter

        setSpinners()

        chb_by_me.setOnCheckedChangeListener { buttonView, isChecked ->
            isCreatedByMe = if (isChecked) "True" else "False"
        }

    }

    private fun setPreSelectedFilters(filters: SelectedFilter?) {

        filters?.eventStatus?.let {
            event_status_spinner.setSelection(it)
        }
        filters?.eventType?.let {
            event_types_spinner.setSelection(it)
        }
        filters?.eventFees?.let {
                event_fees_spinner.setSelection(it)
        }
        filters?.byMe?.let {
            chb_by_me.isChecked= it.toLowerCase().equals("true")
        }

        filters?.startDate?.let {
            edt_start_date.setText(it)
        }

        filters?.endDate?.let {
            edt_end_date.setText(it)
        }



    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.e("Filter on onDestroy")
    }

    override fun onResume() {
        super.onResume()
        val params: ViewGroup.LayoutParams = dialog!!.window!!.attributes
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog!!.window!!.attributes = params as WindowManager.LayoutParams
    }


}





