package `in`.bitspilani.eon.eventOrganiser.ui.adapter

import `in`.bitspilani.eon.eventOrganiser.ui.CalenderFilterFragment
import `in`.bitspilani.eon.eventOrganiser.ui.EventFilterFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter


class FilterPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {


    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return EventFilterFragment()
            1 -> return CalenderFilterFragment()

        }
        return CalenderFilterFragment()
    }

    override fun getItemCount(): Int {
        return 2
    }
}