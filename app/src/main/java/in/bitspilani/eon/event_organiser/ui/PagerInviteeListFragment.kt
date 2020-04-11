package `in`.bitspilani.eon.event_organiser.ui

import `in`.bitspilani.eon.R
import `in`.bitspilani.eon.event_organiser.models.DetailResponseOrganiser
import `in`.bitspilani.eon.event_organiser.models.Invitee
import `in`.bitspilani.eon.event_organiser.ui.adapter.InviteesAdapter
import `in`.bitspilani.eon.utils.MarginItemDecoration
import `in`.bitspilani.eon.utils.clickWithDebounce
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_invitee.*


/**
 * A simple [Fragment] subclass.
 *
 */
class PagerInviteeListFragment(val detailResponseOrganiser: DetailResponseOrganiser) : Fragment(),CallbackListener {

    // tab titles
    private val titles =
        arrayOf("Movies", "Events")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_invitee, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRecyclerview(detailResponseOrganiser)

        fab.clickWithDebounce {
            showDialog()
        }
    }
    private val listOfEvent = mutableListOf<Invitee>()
    var inviteesAdapter =  InviteesAdapter(
        listOfEvent
    )
    private fun setRecyclerview(detailResponseOrganiser: DetailResponseOrganiser) {

        rv_invitee_list.layoutManager = LinearLayoutManager(activity)
        rv_invitee_list.addItemDecoration(MarginItemDecoration(
                resources.getDimension(R.dimen._16sdp).toInt()) )
        inviteesAdapter=
            InviteesAdapter(
                detailResponseOrganiser.data[0].invitee_list
            )
        rv_invitee_list.adapter = inviteesAdapter

    }

    private fun showDialog() {
        val dialogFragment = AddInviteeFragment(this)
        dialogFragment.show(childFragmentManager, "AaddInviteeDialog")
    }
    override fun onDataReceived(data: String) {

        rv_invitee_list.refreshDrawableState()
        inviteesAdapter.notifyDataSetChanged()
        Toast.makeText(activity, "New invitee added successfully.", Toast.LENGTH_LONG)
            .show()

    }


}
