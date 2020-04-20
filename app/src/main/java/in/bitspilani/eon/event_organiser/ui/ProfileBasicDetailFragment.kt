import `in`.bitspilani.eon.R
import `in`.bitspilani.eon.event_organiser.models.EventType
import `in`.bitspilani.eon.event_organiser.models.FilterResponse
import `in`.bitspilani.eon.event_organiser.ui.UserProfileCallBack
import `in`.bitspilani.eon.event_organiser.viewmodel.UserProfileViewModel
import `in`.bitspilani.eon.login.data.Data
import `in`.bitspilani.eon.utils.*
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.fragment_profile_basic_detail.*

/**
 * A simple [Fragment] subclass.
 */
class ProfileBasicDetailFragment(val userProfileCallBack: UserProfileCallBack) : DialogFragment() {

    private val userProfileViewModel by viewModels<UserProfileViewModel> { getViewModelFactory() }

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
        val eventTypeCached = ModelPreferencesManager.get<FilterResponse>(Constants.EVENT_TYPES)
        if(eventTypeCached!=null && ModelPreferencesManager.getInt(Constants.USER_ROLE)==2)
            populateFilters(eventTypeCached.data)

        setUpClickListeners()
        setObservables()



    }

    private fun setObservables() {
        userProfileViewModel.basicDetailLiveData.observe(viewLifecycleOwner, Observer {

            progress_bar.visibility=View.GONE
            Toast.makeText(activity, "User details updated successfully", Toast.LENGTH_LONG).show()
            dismiss()
        })
    }

    private fun setUpClickListeners() {
        btn_close.clickWithDebounce { dismiss() }
        btn_basic_cancel.clickWithDebounce {   dismiss() }
        btn_basic_confirm.clickWithDebounce {
            validateDataAndConfirm()

        }
        setData()
    }

    private fun validateDataAndConfirm() {

        if (Validator.isValidName(rdt_basic_name)
            && Validator.isValidName(edt_basic_address)
            && Validator.isValidPhone(rdt_basic_contact))
        {

            val userData = ModelPreferencesManager.get<Data>(Constants.CURRENT_USER)

            userProfileViewModel.updateUserDetails(userData?.user?.user_id!!,
                name =rdt_basic_name.text.toString(),
                contact = rdt_basic_contact.text.toString(),
                address = edt_basic_address.text.toString())
            progress_bar.visibility=View.VISIBLE
        }


    }

    private fun setData() {
        val userData = ModelPreferencesManager.get<Data>(Constants.CURRENT_USER)
        rdt_basic_name.setText(userData?.user?.name, TextView.BufferType.EDITABLE)
        rdt_basic_email.setText(userData?.user?.email, TextView.BufferType.EDITABLE)
        edt_basic_address.setText(userData?.user?.address, TextView.BufferType.EDITABLE)
        rdt_basic_contact.setText(userData?.user?.contact_number.toString(), TextView.BufferType.EDITABLE)
    }

    private fun populateFilters(eventTypeList: List<EventType>) {
        for (i in eventTypeList) {
            val radioButton = layoutInflater.inflate(R.layout.item_radio_button, radio_group_user, false) as Chip
            radioButton.id = i.id
            radioButton.text = i.type
            radio_group_user.addView(radioButton)
            radioButton.setOnClickListener {
                filterType = it.id
            }
        }
    }

}