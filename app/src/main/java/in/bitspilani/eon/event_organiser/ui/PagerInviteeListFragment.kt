package `in`.bitspilani.eon.event_organiser.ui

import `in`.bitspilani.eon.R
import `in`.bitspilani.eon.event_organiser.models.DetailResponseOrganiser
import `in`.bitspilani.eon.event_organiser.ui.adapter.InviteesAdapter
import `in`.bitspilani.eon.utils.MarginItemDecoration
import `in`.bitspilani.eon.utils.clickWithDebounce
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_invitee.*


/**
 * A simple [Fragment] subclass.
 *
 */
class PagerInviteeListFragment(val detailResponseOrganiser: DetailResponseOrganiser) : Fragment(),CallbackListener {

    private lateinit var  layoutManager: LinearLayoutManager


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
        setUpSearch()

        fab.clickWithDebounce {
            showDialog()
        }
    }

    private fun setUpSearch() {
        invitee_search_view.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                inviteesAdapter.filter.filter(newText)
                return false
            }

        })
    }

    var inviteesAdapter =  InviteesAdapter(arrayListOf())
    private fun setRecyclerview(detailResponseOrganiser: DetailResponseOrganiser) {

        layoutManager = LinearLayoutManager(activity)
        rv_invitee_list.layoutManager = layoutManager
        rv_invitee_list.adapter = InviteesAdapter(detailResponseOrganiser.data[0].invitee_list)

    }

    private fun showDialog() {
        val dialogFragment = AddInviteeFragment(detailResponseOrganiser.data[0])
        dialogFragment.show(childFragmentManager, "AaddInviteeDialog")
    }
    override fun onDataReceived(data: String) {

        rv_invitee_list.refreshDrawableState()
        inviteesAdapter.notifyDataSetChanged()
        Toast.makeText(activity, "New invitee added successfully.", Toast.LENGTH_LONG)
            .show()

    }


}
