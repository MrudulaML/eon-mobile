package `in`.bitspilani.eon.eventOrganiser.ui.adapter

import `in`.bitspilani.eon.eventOrganiser.ui.EventFragment
import `in`.bitspilani.eon.eventOrganiser.ui.InviteeFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter


class EventDetailPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {


    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return EventFragment()
            1 -> return InviteeFragment()

        }
        return EventFragment()
    }

    override fun getItemCount(): Int {
        return 2
    }
}