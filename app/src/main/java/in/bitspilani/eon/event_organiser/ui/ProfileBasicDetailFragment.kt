import `in`.bitspilani.eon.R
import `in`.bitspilani.eon.event_organiser.models.EventType
import `in`.bitspilani.eon.event_organiser.models.FilterResponse
import `in`.bitspilani.eon.event_organiser.ui.CallbackListener
import `in`.bitspilani.eon.login.data.Data
import `in`.bitspilani.eon.utils.Constants
import `in`.bitspilani.eon.utils.ModelPreferencesManager
import `in`.bitspilani.eon.utils.clickWithDebounce
import android.os.Bundle
import android.view.*
import android.widget.CheckBox
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_filter_event.*
import kotlinx.android.synthetic.main.fragment_profile_basic_detail.*

/**
 * A simple [Fragment] subclass.
 */
class ProfileBasicDetailFragment() : DialogFragment() {

    private var filterType: Int? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isCancelable = false
        return inflater.inflate(R.layout.fragment_profile_basic_detail, container, false)
    }
    override fun getTheme(): Int {
        return R.style.DialogTheme
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //send data
        //callbackListener.onDataReceived(editText.text.toString())
        val eventTypeCached = ModelPreferencesManager.get<FilterResponse>(Constants.EVENT_TYPES)

        if(eventTypeCached!=null && ModelPreferencesManager.getInt(Constants.USER_ROLE)==0)
            populateFilters(eventTypeCached.data)
        btn_close.clickWithDebounce { dismiss() }
        btn_basic_cancel.clickWithDebounce {   dismiss() }
        btn_basic_confirm.clickWithDebounce {

            dismiss()

            Toast.makeText(activity, "Saved successfully", Toast.LENGTH_LONG)
                .show()

        }

        setdata()

    }

    private fun setdata() {
        val userData = ModelPreferencesManager.get<Data>(Constants.CURRENT_USER)
        rdt_basic_name.setText(userData?.user?.name, TextView.BufferType.EDITABLE)
        rdt_basic_email.setText(userData?.user?.email, TextView.BufferType.EDITABLE)
    }

    private fun populateFilters(eventTypeList: List<EventType>) {
        for (i in 0..4) {
            val radioButton = layoutInflater.inflate(R.layout.item_radio_button, checkbox_layout, false)
            val radio = radioButton.findViewById(R.id.rb_item) as RadioButton

            radio.text = eventTypeList[i].type
            checkbox_layout.addView(radioButton)

        }
        interest.visibility=View.VISIBLE

    }

}