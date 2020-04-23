package `in`.bitspilani.eon.event_organiser.ui

import `in`.bitspilani.eon.BitsEonActivity
import `in`.bitspilani.eon.R
import `in`.bitspilani.eon.event_organiser.ui.adapter.NotificationAdapter
import `in`.bitspilani.eon.event_organiser.viewmodel.NotificationViewModel
import `in`.bitspilani.eon.login.ui.ActionbarHost
import `in`.bitspilani.eon.utils.MarginItemDecoration
import `in`.bitspilani.eon.utils.clickWithDebounce
import `in`.bitspilani.eon.utils.getViewModelFactory
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_notification.*


/**
 * A simple [Fragment] subclass.
 */
class ToolbarNotificationFragment : Fragment() {


    private val notificationViewModel by viewModels<NotificationViewModel> { getViewModelFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        (activity as AppCompatActivity?)!!.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_notification, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setUpObservable()
        setClicks()
    }

    private fun setUpObservable() {

        notificationViewModel.notificationLiveData.observe(viewLifecycleOwner, Observer {

            rv_notifications.layoutManager = LinearLayoutManager(activity)
            rv_notifications.addItemDecoration(
                MarginItemDecoration(
                    resources.getDimension(R.dimen._16sdp).toInt()
                )
            )
            val notificationAdapter =
                NotificationAdapter(
                    it.data
                )
            rv_notifications.adapter = notificationAdapter
        })

        notificationViewModel.clearAllData.observe(viewLifecycleOwner, Observer {


            showUserMsg(it.message)
            notificationViewModel.getNotification()

        })

        notificationViewModel.progressLiveData.observe(viewLifecycleOwner, Observer {

            (activity as BitsEonActivity).showProgress(it)
        })

    }

    fun showUserMsg(msg: String) {
        Toast.makeText(activity, msg, Toast.LENGTH_LONG).show()
    }

    fun setClicks() {

        tv_clear_all.clickWithDebounce {

            notificationViewModel.readNotification()
        }
    }

    /**
     * toggle visibility of different navigation
     */
    private var actionbarHost: ActionbarHost? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ActionbarHost) {
            actionbarHost = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        actionbarHost?.showToolbar(showToolbar = true, showBottomNav = false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        setHasOptionsMenu(false)
        (activity as AppCompatActivity?)!!.supportActionBar!!.setDisplayHomeAsUpEnabled(false)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val activity = activity as? BitsEonActivity
        return when (item.itemId) {
            android.R.id.home -> {
                activity?.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initView() {
        //dummy list
        actionbarHost?.showToolbar(
            showToolbar = true,
            title = "My Notifications",
            showBottomNav = false
        )

        notificationViewModel.getNotification()


    }


}
