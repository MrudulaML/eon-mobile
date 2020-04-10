package `in`.bitspilani.eon.eventOrganiser.ui.adapter

import `in`.bitspilani.eon.eventOrganiser.ui.PagerEventFragment
import `in`.bitspilani.eon.eventOrganiser.ui.PagerInviteeListFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter


class EventDetailPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {


    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return PagerEventFragment()
            1 -> return PagerInviteeListFragment()

        }
        return PagerEventFragment()
    }

    override fun getItemCount(): Int {
        return 2
    }
}