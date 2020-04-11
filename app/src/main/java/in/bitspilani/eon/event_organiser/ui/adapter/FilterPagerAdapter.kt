package `in`.bitspilani.eon.event_organiser.ui.adapter

import `in`.bitspilani.eon.event_organiser.ui.FilterCalenderFragment
import `in`.bitspilani.eon.event_organiser.ui.FilterDialogFragment
import `in`.bitspilani.eon.event_organiser.ui.FilterEventFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter


class FilterPagerAdapter(
    fragmentActivity: FragmentActivity,
    private val filterDialogFragment: FilterDialogFragment
) :
    FragmentStateAdapter(fragmentActivity) {


    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return FilterEventFragment(filterDialogFragment)
            1 -> return FilterCalenderFragment(filterDialogFragment)

        }
        return FilterCalenderFragment(filterDialogFragment)
    }

    override fun getItemCount(): Int {
        return 2
    }
}