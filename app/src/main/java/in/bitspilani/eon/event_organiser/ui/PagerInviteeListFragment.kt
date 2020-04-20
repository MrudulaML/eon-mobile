package `in`.bitspilani.eon.event_organiser.ui

import `in`.bitspilani.eon.R
import `in`.bitspilani.eon.event_organiser.models.DetailResponseOrganiser
import `in`.bitspilani.eon.event_organiser.models.Invitee
import `in`.bitspilani.eon.event_organiser.ui.adapter.EventDetailPagerAdapter
import `in`.bitspilani.eon.event_organiser.ui.adapter.InviteesAdapter
import `in`.bitspilani.eon.event_organiser.viewmodel.EventDetailOrganiserViewModel
import `in`.bitspilani.eon.utils.clickWithDebounce
import `in`.bitspilani.eon.utils.getViewModelFactory
import `in`.bitspilani.eon.utils.goneUnless
import `in`.bitspilani.eon.utils.showSnackbar
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_event_detail_organiser.*
import kotlinx.android.synthetic.main.fragment_invitee.*
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList


/**
 * A simple [Fragment] subclass.
 *
 */
class PagerInviteeListFragment(private val detailResponse: DetailResponseOrganiser) : Fragment(),
    CallbackListener {

    private var deleteAll: Boolean = false
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var inviteeList: ArrayList<Invitee>
    private var inviteeListDeleted: ArrayList<Invitee> = arrayListOf()


    private val eventDetailOrganiserViewModel by viewModels<EventDetailOrganiserViewModel> { getViewModelFactory() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_invitee, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRecyclerview(detailResponse)
        setUpSearch()

        fab.clickWithDebounce {
            showDialog()
        }

        eventDetailOrganiserViewModel.deleteInvitee.observe(viewLifecycleOwner, Observer {


        if(it.message.toLowerCase(Locale.ROOT).contains("success")) {
            showDeleteOption(false)
            if(chb_select_all.isChecked)
                chb_select_all.isChecked=false
            view.showSnackbar(it.message, 0)
            if(deleteAll)
                eventDetailOrganiserViewModel.getEventDetails(detailResponse.data.id)

        }

        })
        chb_select_all.setOnCheckedChangeListener { buttonView, isChecked ->

            deleteAll = if (isChecked) {
                inviteesAdapter.selectAll()
                showDeleteOption(true)
                true
            } else {
                inviteesAdapter.deSelectAll()
                showDeleteOption(false)
                false
            }
        }

        btn_delete_all.clickWithDebounce {

            inviteeList.clear()
            if (!deleteAll) {
                val invitationIds: ArrayList<Int> = arrayListOf()
                for (item in inviteeListDeleted)
                    invitationIds.add(item.invitation_id)
                eventDetailOrganiserViewModel.deleteInvitee(
                    invitationIds,
                    detailResponse.data.id
                )
               eventDetailOrganiserViewModel.getEventDetails(detailResponse.data.id)
            } else {

                val invitationIds: ArrayList<Int> = arrayListOf()
                for (item in inviteeList)
                    invitationIds.add(item.invitation_id)
                eventDetailOrganiserViewModel.deleteInvitee(
                    invitationIds,
                    detailResponse.data.id
                )
            }
        }

        eventDetailOrganiserViewModel.eventData.observe(viewLifecycleOwner, Observer {

            inviteeList = it.data.invitee_list
            inviteesAdapter = InviteesAdapter(
                inviteeList
            ,selectCheckBoxCallback = { invitee: Invitee, isSelected: Boolean ->
                Timber.e("invitee id${invitee.invitation_id}")
                if (isSelected)
                    inviteeListDeleted.add(invitee)
                else
                    inviteeListDeleted.remove(invitee)
                showDeleteOption(inviteeListDeleted.size > 0)
                Timber.e("invitee selected list${inviteeListDeleted.size}")
            })
            rv_invitee_list.adapter = inviteesAdapter
            inviteesAdapter.notifyDataSetChanged()


        })

        eventDetailOrganiserViewModel.deleteProgress.observe(viewLifecycleOwner, Observer {

            progress_bar.goneUnless(it)
        })
    }

    private fun showDeleteOption(show: Boolean) {

        btn_delete_all.goneUnless(show)
    }


    private fun setUpSearch() {
        invitee_search_view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                //TODO dont do this chaos fix this on time Ashutosh
                inviteesAdapter.filter.filter(newText)
                return false
            }

        })
    }

    var inviteesAdapter = InviteesAdapter(arrayListOf(), { invitee: Invitee, b: Boolean -> })
    private fun setRecyclerview(detailResponseOrganiser: DetailResponseOrganiser) {

        layoutManager = LinearLayoutManager(activity)
        rv_invitee_list.layoutManager = layoutManager
        inviteeList = detailResponseOrganiser.data.invitee_list
        inviteesAdapter = InviteesAdapter(
            inviteeList,
            selectCheckBoxCallback = { invitee: Invitee, isSelected: Boolean ->
                Timber.e("invitee id${invitee.invitation_id}")
                if (isSelected)
                    inviteeListDeleted.add(invitee)
                else
                    inviteeListDeleted.remove(invitee)
                showDeleteOption(inviteeListDeleted.size > 0)
                Timber.e("invitee selected list${inviteeListDeleted.size}")
            })
        rv_invitee_list.adapter = inviteesAdapter

    }

    private fun showDialog() {
        val dialogFragment = AddInviteeFragment(detailResponse.data, this)
        dialogFragment.show(childFragmentManager, "AaddInviteeDialog")
    }

    override fun onDataReceived(inviteeLis: ArrayList<Invitee>) {

        eventDetailOrganiserViewModel.getEventDetails(detailResponse.data.id)

    }


}
