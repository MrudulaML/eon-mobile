package `in`.bitspilani.eon.analytics

import `in`.bitspilani.eon.R
import `in`.bitspilani.eon.analytics.data.AnalyticsResponse
import `in`.bitspilani.eon.analytics.data.OrganizedEvent
import `in`.bitspilani.eon.databinding.FragmentAnalyticsDashboardBinding
import `in`.bitspilani.eon.login.ui.ActionbarHost
import `in`.bitspilani.eon.utils.defocusAndHideKeyboard
import `in`.bitspilani.eon.utils.getViewModelFactory
import `in`.bitspilani.eon.utils.hideKeyboard
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ArrayAdapter
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_analytics_dashboard.*
import kotlinx.android.synthetic.main.fragment_dashboard.*

class AnalyticsDashboardFragment : Fragment() {

    private lateinit var  layoutManager: LinearLayoutManager
    private val analyticsViewModel by viewModels<AnalyticsViewModel> { getViewModelFactory() }
    lateinit var binding: FragmentAnalyticsDashboardBinding
    private lateinit var analyticsData :AnalyticsResponse
    private lateinit var organizedEventAdapter: OrganizedEventAdapter
    private var actionbarHost: ActionbarHost? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity!!.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        analyticsViewModel.getAnalytics()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_analytics_dashboard, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpObservables()
        setUpSpinner()
        setUpSearch()
        actionbarHost?.showToolbar(showToolbar = true,title = "Analytics",showBottomNav = true)

    }

    private fun setUpSpinner() {
        val items = arrayOf("Completed", "Ongoing", "Cancelled")
        val adapter = ArrayAdapter(activity!!, android.R.layout.simple_spinner_dropdown_item, items)
        spinner_event_type.prompt = "Status"
        spinner_event_type.adapter = adapter
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
            analyticsData=it
            binding.analyticsData=analyticsData
            setRecyclerView(it.data.event_list)
        })
    }

    private fun setRecyclerView(eventList: ArrayList<OrganizedEvent>) {
        layoutManager = LinearLayoutManager(activity)
        rv_event_list_analytics.layoutManager = layoutManager
        organizedEventAdapter=OrganizedEventAdapter(eventList)
        rv_event_list_analytics.adapter = organizedEventAdapter
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