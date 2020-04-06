package `in`.bitspilani.eon.eventOrganiser.ui



import `in`.bitspilani.eon.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_event_detail.*


/**
 * A simple [Fragment] subclass.
 *
 */
class EventDetailsFragment : Fragment() {

    // tab titles
    private val titles =
        arrayOf("Movies", "Events")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_detail, container, false)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity?)!!.supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        event_detail_view_pager.adapter= EventDetailPagerAdapter(activity!!)
        TabLayoutMediator(tab_layout, event_detail_view_pager) { tab, position ->
            //To get the first name of doppelganger celebrities
            tab.text = titles[position]
        }.attach()
    }



}
