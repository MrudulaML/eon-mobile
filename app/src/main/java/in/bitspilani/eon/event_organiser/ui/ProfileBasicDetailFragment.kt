import `in`.bitspilani.eon.BitsEonActivity
import `in`.bitspilani.eon.R
import `in`.bitspilani.eon.event_organiser.models.EventType
import `in`.bitspilani.eon.event_organiser.models.FilterResponse
import `in`.bitspilani.eon.event_organiser.ui.UserProfileCallBack
import `in`.bitspilani.eon.event_organiser.viewmodel.UserProfileViewModel
import `in`.bitspilani.eon.login.data.Data
import `in`.bitspilani.eon.login.data.User
import `in`.bitspilani.eon.login.data.UserInfo
import `in`.bitspilani.eon.utils.*
import android.os.Bundle
import android.util.Log
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
import com.google.gson.JsonArray
import kotlinx.android.synthetic.main.fragment_profile_basic_detail.*
import timber.log.Timber

/**
 * A simple [Fragment] subclass.
 */
class ProfileBasicDetailFragment(val userProfileCallBack: UserProfileCallBack) : DialogFragment() {

    private val userProfileViewModel by viewModels<UserProfileViewModel> { getViewModelFactory() }
    private val interestList: ArrayList<Int> = arrayListOf()
    private var filterType: Int? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isCancelable = false
        return inflater.inflate(R.layout.fragment_profile_basic_detail, container, false)
    }

    var userType: Int=0
    override fun getTheme(): Int {
        return R.style.DialogTheme
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val eventTypeCached = ModelPreferencesManager.get<FilterResponse>(Constants.EVENT_TYPES)
        if (eventTypeCached != null && ModelPreferencesManager.getInt(Constants.USER_ROLE) == 2)
            populateFilters(eventTypeCached.data)
        init()
        setUpClickListeners()
        setObservables()
    }

    var userData : Data?=null
    fun init() {

        progress_bar.visibility = View.VISIBLE

        userData = ModelPreferencesManager.get<Data>(Constants.CURRENT_USER)

        userType=userData!!.user.role.id


            userProfileViewModel.getUserDetails(userData!!.user.user_id)

    }


    private fun setObservables() {
        userProfileViewModel.basicDetailUpdateLiveData.observe(viewLifecycleOwner, Observer {


            progress_bar.visibility = View.GONE

            setUpdatedData(it.data)

            Toast.makeText(activity, "User details updated successfully", Toast.LENGTH_LONG).show()


        })

        userProfileViewModel.basicDetailGetLiveData.observe(viewLifecycleOwner, Observer {

            progress_bar.visibility = View.GONE

            setData(it.data)

        })


        //loader observable
        userProfileViewModel.progressLiveData.observe(viewLifecycleOwner, Observer {

            (activity as BitsEonActivity).showProgress(it)
        })

    }


    private fun setData( user: UserInfo) {
        rdt_basic_email.isEnabled = false
        try {

            if (userType == 1) {

                layout_basic_name.visibility = View.GONE
                layout_org_name.visibility = View.VISIBLE
                if (user.organization == null) {
                    rdt_org_name.hint = "Organization Name"
                } else {
                    rdt_org_name.hint = ""
                    rdt_org_name.setText(user.organization, TextView.BufferType.EDITABLE)
                }

            } else {

                layout_basic_name.visibility - View.VISIBLE
                layout_org_name.visibility = View.GONE

                if (user.name == null) {
                    rdt_basic_name.hint = "User Name"
                } else {
                    rdt_basic_name.hint = ""
                    rdt_basic_name.setText(user.name, TextView.BufferType.EDITABLE)
                }


            }


            if(userType==2)
            updateInterests(user.interest)

            rdt_basic_email.setText(userData!!.user.email, TextView.BufferType.EDITABLE)
            edt_basic_address.setText(user.address, TextView.BufferType.EDITABLE)
            rdt_basic_contact.setText(
                user?.contact_number.toString(),
                TextView.BufferType.EDITABLE
            )
        } catch (e: Exception) {

        }
    }

    private fun setUpdatedData(data: UserInfo) {


        rdt_basic_name.setText(data.name, TextView.BufferType.EDITABLE)
        edt_basic_address.setText(data.address, TextView.BufferType.EDITABLE)
        rdt_basic_contact.setText(
            data.contact_number,
            TextView.BufferType.EDITABLE
        )

        if(userType==2)
        updateInterests(data.interest)


    }

    fun updateInterests(interests: List<Int>?){


        if (interests != null) {


                interests.forEach {


                    val chip = chip_group.getChildAt(it - 1) as Chip

                    chip.isChecked = true

                }
        }

    }

    private fun setUpClickListeners() {
        btn_close.clickWithDebounce { dismiss() }
        btn_basic_cancel.clickWithDebounce { dismiss() }
        btn_basic_confirm.clickWithDebounce {
            validateDataAndConfirm()

        }
    }

    private fun validateDataAndConfirm() {

        if ((Validator.isValidName(rdt_org_name, true) || Validator.isValidName(
                rdt_basic_name,
                true
            )) && Validator.isValidPhone(rdt_basic_contact, true) && isContactNumberValid()
        ) {

            val userData = ModelPreferencesManager.get<Data>(Constants.CURRENT_USER)

            for (i in 0 until chip_group.childCount) {
                val chip = chip_group.getChildAt(i) as Chip
                if (chip.isChecked) {
                    interestList.add(chip_group.getChildAt(i).id)
                }
            }
            val array = JsonArray()
            for (each in interestList) {
                array.add(each)
            }
            Timber.e("interest list$array")

            userProfileViewModel.updateUserDetails(
                userData?.user?.user_id!!,
                organization = rdt_org_name.text.toString(),
                name = rdt_basic_name.text.toString(),
                contact = rdt_basic_contact.text.toString(),
                address = edt_basic_address.text.toString(),
                interestList = array
            )
            progress_bar.visibility = View.VISIBLE
        }


    }

    fun isContactNumberValid(): Boolean {


        if (rdt_basic_contact.text!!.length == 10) {
            return true
        } else
            showUserMsg("Please enter 10 digit mobile number")

        return false

    }

    fun showUserMsg(msg: String) {

        Toast.makeText(activity!!, msg, Toast.LENGTH_LONG).show()
    }



    private fun populateFilters(eventTypeList: List<EventType>) {
        for (i in eventTypeList) {
            val chip =
                layoutInflater.inflate(R.layout.item_radio_button, chip_group, false) as Chip
            chip.id = i.id
            chip.text = i.type
            chip_group.addView(chip)
            Timber.e("interest sze${interestList.size}")
        }

    }


}