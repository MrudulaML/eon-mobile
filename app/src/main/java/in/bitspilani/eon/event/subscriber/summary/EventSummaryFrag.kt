package `in`.bitspilani.eon.event.subscriber.summary

import `in`.bitspilani.eon.R
import `in`.bitspilani.eon.utils.clickWithDebounce
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.event_summary_fragment.*


class EventSummaryFrag : Fragment() {

    companion object {
        fun newInstance() = EventSummaryFrag()
    }

    private lateinit var viewModel: EventSummaryViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.event_summary_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(EventSummaryViewModel::class.java)
        // TODO: Use the ViewModel
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_pay.clickWithDebounce {

            findNavController().navigate(R.id.paymentFrag)
        }

    }
}
