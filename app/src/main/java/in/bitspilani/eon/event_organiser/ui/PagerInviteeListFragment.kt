package `in`.bitspilani.eon.event_organiser.ui

import `in`.bitspilani.eon.R
import `in`.bitspilani.eon.event_organiser.models.DetailResponseOrganiser
import `in`.bitspilani.eon.event_organiser.models.Invitee
import `in`.bitspilani.eon.event_organiser.ui.adapter.InviteesAdapter
import `in`.bitspilani.eon.event_organiser.viewmodel.EventDetailOrganiserViewModel
import `in`.bitspilani.eon.utils.clickWithDebounce
import `in`.bitspilani.eon.utils.getViewModelFactory
import `in`.bitspilani.eon.utils.goneUnless
import `in`.bitspilani.eon.utils.showSnackbar
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_invitee.*
import timber.log.Timber
import java.util.*


/**
 * A simple [Fragment] subclass.
 *
 */
class PagerInviteeListFragment(private val detailResponse: DetailResponseOrganiser, private val inviteeCallbackListener: InviteeCallbackListener) : Fragment(),
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
        setUpVisibility()
        fab.clickWithDebounce {
            showDialog()
        }

        eventDetailOrganiserViewModel.deleteInvitee.observe(viewLifecycleOwner, Observer {


            if (it.message.toLowerCase(Locale.ROOT).contains("success")) {
                showDeleteOption(false)
                if (!deleteAll)
                    inviteeListDeleted.clear()
            }
            view.showSnackbar(it.message, 0)

        })
        chb_select_all.setOnCheckedChangeListener { buttonView, isChecked ->

            if (isChecked) {
                inviteesAdapter.selectAll()
                showDeleteOption(true)
                deleteAll = true

            } else {
                inviteesAdapter.deSelectAll()
                showDeleteOption(false)
                deleteAll = false

            }
        }
        btn_delete_all.clickWithDebounce {

            if (!deleteAll) {
                val invitationIds: ArrayList<Int> = arrayListOf()
                for (item in inviteeListDeleted)
                    invitationIds.add(item.invitation_id)
                eventDetailOrganiserViewModel.deleteInvitee(
                    invitationIds,
                    detailResponse.data.id
                )
                inviteesAdapter.deSelectAll()
               eventDetailOrganiserViewModel.getEventDetails(detailResponse.data.id)
            } else {
                val invitationIds: ArrayList<Int> = arrayListOf()
                for (i in inviteeList) {
                    invitationIds.add(i.invitation_id)
                }
                eventDetailOrganiserViewModel.deleteInvitee(
                    invitationIds,
                    detailResponse.data.id
                )

                eventDetailOrganiserViewModel.getEventDetails(detailResponse.data.id)
            }
        }

        btn_close.clickWithDebounce {

            inviteesAdapter.deSelectAll()
            showDeleteOption(false)
            inviteeListDeleted.clear()
        }

        eventDetailOrganiserViewModel.eventData.observe(viewLifecycleOwner, Observer {
            progress_bar.goneUnless(false)
            chb_select_all.isChecked=false
            inviteeList = it.data.invitee_list
            invitee_search_view.goneUnless(inviteeList.size>0)
            inviteesAdapter = InviteesAdapter(
                inviteeList,
                detailResponse.data.event_status,
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
            inviteesAdapter.notifyDataSetChanged()


        })

        eventDetailOrganiserViewModel.deleteProgress.observe(viewLifecycleOwner, Observer {

            progress_bar.goneUnless(it)
        })
    }


    //TODO fix this with data binding
    private fun setUpVisibility() {
        if (detailResponse.data.self_organised) {

            if (detailResponse.data.event_status.toLowerCase().equals("completed") ||
                detailResponse.data.event_status.toLowerCase(Locale.ROOT).equals("cancelled")
            ) {

                fab.visibility = View.GONE
            } else {

                fab.visibility = View.VISIBLE
            }
        }
    }


    private fun showDeleteOption(show: Boolean) {
        if (show) {
            delete_menu.visibility = View.VISIBLE
            inviteeCallbackListener.getDelete(true)
        } else {

            delete_menu.visibility = View.GONE
            inviteeCallbackListener.getDelete(false)
        }


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

   lateinit var inviteesAdapter : InviteesAdapter
    private fun setRecyclerview(detailResponseOrganiser: DetailResponseOrganiser) {

        invitee_search_view.goneUnless(detailResponseOrganiser.data.invitee_list.size>0)
        layoutManager = LinearLayoutManager(activity)
        rv_invitee_list.layoutManager = layoutManager
        inviteeList = detailResponseOrganiser.data.invitee_list
        inviteesAdapter = InviteesAdapter(
            inviteeList,
            detailResponse.data.event_status,
            selectCheckBoxCallback = { invitee: Invitee, isSelected: Boolean ->
                Timber.e("invitee id${invitee.invitation_id}")
                if (isSelected)
                    inviteeListDeleted.add(invitee)
                else
                    inviteeListDeleted.remove(invitee)

                showDeleteOption(inviteeListDeleted.size > 0)
                inviteeCallbackListener.getDelete(inviteeListDeleted.size > 0)

                Timber.e("invitee selected list${inviteeListDeleted.size}")
            })
        rv_invitee_list.adapter = inviteesAdapter

    }

    private fun showDialog() {
        val dialogFragment = AddInviteeFragment(detailResponse.data, this)
        dialogFragment.show(childFragmentManager, "AaddInviteeDialog")
    }

    override fun onDataReceived(inviteeLis: ArrayList<Invitee>) {

        progress_bar.goneUnless(true)
        invitee_search_view.goneUnless(inviteeLis.size>0)
        eventDetailOrganiserViewModel.getEventDetails(detailResponse.data.id)

    }


}
