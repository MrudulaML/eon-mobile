package `in`.bitspilani.eon.event.subscriber.detail

import `in`.bitspilani.eon.R
import `in`.bitspilani.eon.login.ui.ActionbarHost
import `in`.bitspilani.eon.utils.clickWithDebounce
import android.content.Context
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.layout_seat_booking.*


class EventDetails : Fragment() {

    companion object {
        fun newInstance() =
            EventDetails()
    }

    var seatCount: MutableLiveData<Int> = MutableLiveData()
    private lateinit var viewModel: EventDetailsViewModel
    private var actionbarHost: ActionbarHost? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.event_details_fragment, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(EventDetailsViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDummyCounterLogic()

        actionbarHost?.showToolbar(showToolbar = false,showBottomNav = false)

        tv_seat_counter.text = 1.toString()

        seatCount.observe(this, Observer {

            tv_seat_counter.text = count.toString()
            btn_price.text = "₹ " + (count * 500)
        })

        btn_price.clickWithDebounce {

            findNavController().navigate(R.id.eventSummaryFrag)

        }
    }

    var count = 1
    fun setDummyCounterLogic() {


        iv_increment.setOnClickListener {

            seatCount.postValue(count++)

        }

        iv_decrement.setOnClickListener {

            if (count > 0) seatCount.postValue(count--)

        }


    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ActionbarHost) {
            actionbarHost = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        actionbarHost?.showToolbar(showToolbar = true,showBottomNav = true)
    }


}
