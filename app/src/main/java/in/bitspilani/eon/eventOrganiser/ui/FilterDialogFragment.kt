package `in`.bitspilani.eon.eventOrganiser.ui


import `in`.bitspilani.eon.R
import `in`.bitspilani.eon.eventOrganiser.data.Invitee
import `in`.bitspilani.eon.utils.MarginItemDecoration
import `in`.bitspilani.eon.utils.clickWithDebounce
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_add_invitee.*
import kotlinx.android.synthetic.main.fragment_event_detail.*
import kotlinx.android.synthetic.main.fragment_filter_dialog.*
import kotlinx.android.synthetic.main.fragment_invitee.*


/**
 * A simple [Fragment] subclass.
 *
 */
class FilterDialogFragment(private val callbackListener: CallbackListener) : DialogFragment() {

    // tab titles
    private val titles =
        arrayOf("Type of Events", "Calender")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isCancelable = true
        return inflater.inflate(R.layout.fragment_filter_dialog, container, false)
    }

    override fun getTheme(): Int {
        return R.style.DialogTheme
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        filter_view_pager.adapter= FilterPagerAdapter(activity!!)
        TabLayoutMediator(filter_tab_layout, filter_view_pager) { tab, position ->
            //To get the first name of doppelganger celebrities
            tab.text = titles[position]
        }.attach()

    }


}