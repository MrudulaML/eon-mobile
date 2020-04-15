package `in`.bitspilani.eon.event_organiser.ui

import `in`.bitspilani.eon.R
import `in`.bitspilani.eon.event_organiser.models.DetailResponseOrganiser
import `in`.bitspilani.eon.event_organiser.models.Invitee
import `in`.bitspilani.eon.event_organiser.ui.adapter.InviteesAdapter
import `in`.bitspilani.eon.event_organiser.viewmodel.EventDetailOrganiserViewModel
import `in`.bitspilani.eon.utils.SwipeToDeleteCallback
import `in`.bitspilani.eon.utils.clickWithDebounce
import `in`.bitspilani.eon.utils.getViewModelFactory
import `in`.bitspilani.eon.utils.showSnackbar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_invitee.*
import timber.log.Timber


/**
 * A simple [Fragment] subclass.
 *
 */
class PagerInviteeListFragment(private val detailResponse: DetailResponseOrganiser) : Fragment(),CallbackListener {

    private lateinit var  layoutManager: LinearLayoutManager
    private lateinit var inviteeList: ArrayList<Invitee>
    private var  positionInvitee: Int = 0

    private val eventDetailOrganiserViewModel by viewModels<EventDetailOrganiserViewModel> { getViewModelFactory() }

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

        setRecyclerview(detailResponse)
        setUpSearch()

        fab.clickWithDebounce {
            showDialog()
        }

        eventDetailOrganiserViewModel.deleteInvitee.observe(viewLifecycleOwner, Observer {

            view.showSnackbar(it.message,0)
        })
    }

    private fun setUpSearch() {
        invitee_search_view.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                //TODO dont do this chaos fix this on time Ashutosh
                if(newText.isNullOrEmpty()){

                }
                inviteesAdapter.filter(newText.toString())
                return false
            }

        })
    }

    var inviteesAdapter =  InviteesAdapter(arrayListOf(),itemClickListener = {})
    private fun setRecyclerview(detailResponseOrganiser: DetailResponseOrganiser) {



        layoutManager = LinearLayoutManager(activity)
        rv_invitee_list.layoutManager = layoutManager
        inviteeList=detailResponseOrganiser.data.invitee_list
        inviteesAdapter= InviteesAdapter(inviteeList,itemClickListener = {

            positionInvitee=it
            Timber.e("Position$it")
        })
        rv_invitee_list.adapter = inviteesAdapter

        val swipeHandler = object : SwipeToDeleteCallback(activity!!) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = rv_invitee_list.adapter as InviteesAdapter
                adapter.removeAt(viewHolder.adapterPosition)
                //eventDetailOrganiserViewModel.deleteInvitee(listOf(detailResponse.data.invitee_list.get(positionInvitee+1).invitation_id),detailResponseOrganiser.data.id)
            }


        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(rv_invitee_list)
    }

    private fun showDialog() {
        val dialogFragment = AddInviteeFragment(detailResponse.data,this)
        dialogFragment.show(childFragmentManager, "AaddInviteeDialog")
    }
    override fun onDataReceived(inviteeLis: ArrayList<Invitee>) {

        inviteeList.addAll(inviteeLis)
        inviteesAdapter= InviteesAdapter(inviteeList,itemClickListener = {})
        rv_invitee_list.adapter = inviteesAdapter
        inviteesAdapter.notifyDataSetChanged()
        view?.showSnackbar("it.message",0)

    }


}
