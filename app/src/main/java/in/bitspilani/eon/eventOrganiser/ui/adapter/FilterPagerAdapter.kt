package `in`.bitspilani.eon.eventOrganiser.ui.adapter

import `in`.bitspilani.eon.eventOrganiser.ui.FilterCalenderFragment
import `in`.bitspilani.eon.eventOrganiser.ui.FilterEventFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter


class FilterPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {


    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return FilterEventFragment()
            1 -> return FilterCalenderFragment()

        }
        return FilterCalenderFragment()
    }

    override fun getItemCount(): Int {
        return 2
    }
}