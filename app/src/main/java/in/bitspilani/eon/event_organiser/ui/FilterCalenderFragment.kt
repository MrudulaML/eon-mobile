package `in`.bitspilani.eon.event_organiser.ui

import `in`.bitspilani.eon.R
import `in`.bitspilani.eon.utils.clickWithDebounce
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_filter_calender.*
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class FilterCalenderFragment(private val filterCallbackListener: FilterCallbackListener) : Fragment() {

    private var startDate : String? = null
    private var endDate : String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_filter_calender, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setDatePicker(edt_start_date)
        setDatePicker(edt_end_date)

        btn_filter.clickWithDebounce {
            Timber.e("Filter data calender $startDate")
            startDate=edt_start_date.text.toString()
            endDate=edt_end_date.text.toString()
            filterCallbackListener.onApplyFilter(startDate =startDate, endDate =endDate ,fromFilter = true )
        }
    }

    private fun setDatePicker(dateEditText: EditText) {

        val myCalendar = Calendar.getInstance()

        val datePickerOnDataSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLabel(myCalendar, dateEditText)
        }

        dateEditText.setOnClickListener {
            DatePickerDialog(activity!!, datePickerOnDataSetListener, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun updateLabel(myCalendar: Calendar, dateEditText: EditText) {
        val format = SimpleDateFormat("yyyy-MM-dd")
        dateEditText.setText(format.format(myCalendar.time))
    }




}