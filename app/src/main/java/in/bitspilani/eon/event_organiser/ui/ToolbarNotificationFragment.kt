package `in`.bitspilani.eon.event_organiser.ui

import `in`.bitspilani.eon.BitsEonActivity
import `in`.bitspilani.eon.R
import `in`.bitspilani.eon.event_organiser.models.Notification
import `in`.bitspilani.eon.event_organiser.ui.adapter.NotificationAdapter
import `in`.bitspilani.eon.login.ui.ActionbarHost
import `in`.bitspilani.eon.utils.MarginItemDecoration
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_notification.*


/**
 * A simple [Fragment] subclass.
 */
class ToolbarNotificationFragment : Fragment() {

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
        actionbarHost?.showToolbar(showToolbar = true,showBottomNav = true)
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
        actionbarHost?.showToolbar(showToolbar = true,title = "My Notifications",showBottomNav = false)


        val listOfEvent = mutableListOf<Notification>()
        listOfEvent.add(
            Notification( "BITS Pilani unveils 16th edition of 'Conquest' startup accelerator",
                "BITS Pilani Hyderabad distributes essentials to poor. The institute has decided to cover residential areas adjacent to its campus at Shameerpet")
        )
        listOfEvent.add(
            Notification( "BITS Pilani unveils 16th edition of 'Conquest' startup accelerator",
                "BITS Pilani Hyderabad distributes essentials to poor. The institute has decided to cover residential areas adjacent to its campus at Shameerpet")
        )
        listOfEvent.add(
            Notification( "BITS Pilani unveils 16th edition of 'Conquest' startup accelerator",
                "BITS Pilani Hyderabad distributes essentials to poor. The institute has decided to cover residential areas adjacent to its campus at Shameerpet")
        )
        listOfEvent.add(
            Notification( "BITS Pilani unveils 16th edition of 'Conquest' startup accelerator",
                "BITS Pilani Hyderabad distributes essentials to poor. The institute has decided to cover residential areas adjacent to its campus at Shameerpet")
        )
        listOfEvent.add(
            Notification( "BITS Pilani unveils 16th edition of 'Conquest' startup accelerator",
                "BITS Pilani Hyderabad distributes essentials to poor. The institute has decided to cover residential areas adjacent to its campus at Shameerpet")
        )
        listOfEvent.add(
            Notification( "BITS Pilani unveils 16th edition of 'Conquest' startup accelerator",
                "BITS Pilani Hyderabad distributes essentials to poor. The institute has decided to cover residential areas adjacent to its campus at Shameerpet")
        )

        rv_notifications.layoutManager = LinearLayoutManager(activity)
        rv_notifications.addItemDecoration(
            MarginItemDecoration(
                resources.getDimension(R.dimen._16sdp).toInt())
        )
        val notificationAdapter =
            NotificationAdapter(
                listOfEvent
            )
        rv_notifications.adapter = notificationAdapter

    }


}
