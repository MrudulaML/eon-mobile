package `in`.bitspilani.eon.analytics

import `in`.bitspilani.eon.BitsEonActivity
import `in`.bitspilani.eon.R
import `in`.bitspilani.eon.analytics.data.Data
import `in`.bitspilani.eon.analytics.data.OrganizedEvent
import `in`.bitspilani.eon.analytics.data.TicketGraphObject
import `in`.bitspilani.eon.login.ui.ActionbarHost
import `in`.bitspilani.eon.utils.*
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager

import com.highsoft.highcharts.common.hichartsclasses.*
import kotlinx.android.synthetic.main.fragment_analytics_dashboard.*
import lecho.lib.hellocharts.model.PieChartData
import lecho.lib.hellocharts.model.SliceValue
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import kotlin.collections.ArrayList


class AnalyticsDashboardFragment : Fragment() {

    private lateinit var  layoutManager: LinearLayoutManager
    private val analyticsViewModel by viewModels<AnalyticsViewModel> { getViewModelFactory() }
    private lateinit var organizedEventAdapter: OrganizedEventAdapter
    private var actionbarHost: ActionbarHost? = null

    private lateinit var options : HIOptions
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity!!.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_analytics_dashboard, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpBarChart()
        analyticsViewModel.getAnalytics("")
        setUpObservables()
        setUpSearch()
        setUpClickListeners()
        actionbarHost?.showToolbar(showToolbar = true,title = "Analytics",showBottomNav = true)

    }

    private fun setUpBarChart() {
        
        val options = HIOptions()
        bar_chart.options=options

    }

    private fun setUpClickListeners() {
        spinner_event_type.clickWithDebounce {

            setUpSpinner()
        }
    }


    private fun setUpSpinner() {
        val builder = AlertDialog.Builder(activity!!)
        builder.setTitle("Status")
        val animals = arrayOf("Completed", "Upcoming", "Cancelled","All")
        builder.setItems(animals) { dialog, which ->
            when (which) {
                0 -> {
                    filterEventList("Completed")
                    dialog.dismiss()
                }
                1 -> {
                    filterEventList("Upcoming")
                    dialog.dismiss()
                }
                2 -> {
                    filterEventList("Cancelled")
                    dialog.dismiss()
                }
                3 -> {
                    filterEventList("")
                    dialog.dismiss()
                }

            }
        }
        val dialog = builder.create()
        dialog.show()

    }

    private fun filterEventList(itemAtPosition: String) {
        spinner_event_type.text = itemAtPosition
        analyticsViewModel.getAnalytics(itemAtPosition)
    }

    private fun setUpSearch() {
        search_analytics.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                hideKeyboard(activity)
                return false
            }


            override fun onQueryTextChange(newText: String?): Boolean {
                organizedEventAdapter.filter.filter(newText.toString())
                return false
            }


        })
    }

    private fun setUpObservables() {
        analyticsViewModel.analyticsLiveData.observe(viewLifecycleOwner, Observer {

            setData(it.data)
            setPieChartData(it.data)
            setRecyclerView(it.data.event_list)

            setUpBarChartWithOptions(it.data.ticket_graph_object)

        })

        analyticsViewModel.errorView.observe(viewLifecycleOwner, Observer {

            view?.showSnackbar(it, 0)

        })

        analyticsViewModel.progressLiveData.observe(viewLifecycleOwner, Observer {

            (activity as BitsEonActivity).showProgress(it)
        })
    }

    private fun setUpBarChartWithOptions(optionsa: TicketGraphObject) {

        doAsync {
            val options = JavaUtilsForBar.getOptions(optionsa)

            uiThread {
                bar_chart.options= options
                bar_chart.reload()
            }
        }


    }

    private fun setPieChartData(data: Data) {

        val pieData :MutableList<SliceValue> = arrayListOf()
        pieData.add(SliceValue(data.ongoing_events.toFloat(), ContextCompat.getColor(activity!!, R.color.orange_pie)))
        pieData.add(SliceValue(data.completed_events.toFloat(), ContextCompat.getColor(activity!!, R.color.green_pie)))
        pieData.add(SliceValue(data.cancelled_events.toFloat(), ContextCompat.getColor(activity!!, R.color.red_pie)))

        val pieChartData = PieChartData(pieData)
        pieChartData.setHasCenterCircle(true).setCenterText1("Total Events\n"+data.total_events)
            .setCenterText1FontSize(16).centerText1Color = Color.parseColor("#808080")
        pie_chart.pieChartData = pieChartData
    }

    @SuppressLint("SetTextI18n")
    private fun setData(data: Data) {
        txt_revenue.text= resources.getString(R.string.rupee_symbol)+data.total_revenue.toString()
        txt_completed_events.text=((data.completed_events.toDouble() / data.total_events) * 100).toInt().toString()+"%"
        txt_ongoing_events.text=((data.ongoing_events.toDouble() / data.total_events) * 100).toInt().toString()+"%"
        txt_cancelled_events.text=((data.cancelled_events.toDouble() / data.total_events) * 100).toInt().toString()+"%"

    }

    private fun setRecyclerView(eventList: ArrayList<OrganizedEvent>) {
        layoutManager = LinearLayoutManager(activity)
        rv_event_list_analytics.layoutManager = layoutManager
        organizedEventAdapter = OrganizedEventAdapter(eventList) {
            tv_no_data.goneUnless(it)
        }
        rv_event_list_analytics.adapter = organizedEventAdapter
        (rv_event_list_analytics.adapter as OrganizedEventAdapter).notifyDataSetChanged()
    }

    /**
     * toggle visibility of different navigation
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ActionbarHost) {
            actionbarHost = context
        }
    }


}
