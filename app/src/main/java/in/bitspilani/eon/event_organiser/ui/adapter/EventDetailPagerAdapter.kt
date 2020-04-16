package `in`.bitspilani.eon.event_organiser.ui.adapter

import `in`.bitspilani.eon.event_organiser.models.DetailResponseOrganiser
import `in`.bitspilani.eon.event_organiser.ui.PagerEventFragment
import `in`.bitspilani.eon.event_organiser.ui.PagerInviteeListFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter


class EventDetailPagerAdapter(
    fragmentActivity: FragmentActivity,
    private val eventDetailResponse: DetailResponseOrganiser
) :
    FragmentStateAdapter(fragmentActivity) {


    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return PagerEventFragment(eventDetailResponse)
            1 -> return PagerInviteeListFragment(eventDetailResponse)

        }
        return PagerEventFragment(eventDetailResponse)
    }

    override fun getItemCount(): Int {
        return if(eventDetailResponse.data.self_organised) 2 else 1
    }
}