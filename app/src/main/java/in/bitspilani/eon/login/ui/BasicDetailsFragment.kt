package `in`.bitspilani.eon.login.ui

import `in`.bitspilani.eon.R
import `in`.bitspilani.eon.databinding.FragmentBasicDetailsBinding
import `in`.bitspilani.eon.utils.*
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_basic_details.*


class BasicDetailsFragment : Fragment() {

    lateinit var authViewModel: AuthViewModel
    lateinit var binding: FragmentBasicDetailsBinding

    var signupMap: HashMap<String, Any> = HashMap<String, Any>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_basic_details, container, false)
        activity?.getWindow()?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        authViewModel = activity?.run {
            ViewModelProviders.of(this).get(AuthViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        initViews()
        btn_next.clickWithDebounce {
            onNextClick()
        }

        setObservers()
    }


    fun setObservers() {

        authViewModel.registerData.observe(viewLifecycleOwner, Observer {

            showUserMsg("Registration Successful")

//            ModelPreferencesManager.putInt(Constants.USER_ROLE, it!!.user!!.role!!.id)
//
//            ModelPreferencesManager.putString(Constants.ACCESS_TOKEN, it.access)
//
//            ModelPreferencesManager.putString(Constants.ACCESS_TOKEN, it.access)
//            ModelPreferencesManager.putString(Constants.REFRESH_TOKEN, it.refresh)
//            ModelPreferencesManager.putInt(Constants.USER_ROLE, it.user.role.id)
//            ModelPreferencesManager.put(it, Constants.CURRENT_USER)

            //  findNavController().navigate(R.id.action_BasicInfoFragment_to_homeFragment)


            ContactAdmin.openDialog(activity!!) {

                findNavController().navigate(R.id.action_BasicInfoFragment_to_signInFragment)
            }

        })
        authViewModel.registerError.observe(viewLifecycleOwner, Observer {

            showUserMsg(it)
        })
    }

    private fun onNextClick() {
        when (authViewModel.registerCurrentStep) {
            OrganiserDetailsSteps.BASIC_DETAILS -> {

                if (authViewModel.userType == USER_TYPE.ORGANISER) {
                    if (!Validator.isValidName(edt_org_name, true)){
                        return
                    }else{

                        if (
                            Validator.isValidEmail(edit_email, true) &&
                            Validator.isValidPhone(edt_org_contact, true) &&
                            Validator.isValidName(edt_org_address, true)
                        ) {
                            authViewModel.registerCurrentStep = OrganiserDetailsSteps.PASSWORD
                            binding.step = OrganiserDetailsSteps.PASSWORD
                            binding.stepView.go(1, true)
                            signupMap.put("email", edit_email.text.toString())
                            signupMap.put("contact", edt_org_contact.text.toString())
                            signupMap.put("address", edt_org_address.text.toString())
                        }
                    }
                }
                else {
                    if (!Validator.isValidName(edt_user_name, true)){
                        return
                    }else{
                        if (
                            Validator.isValidEmail(edit_email, true) &&
                            Validator.isValidPhone(edt_org_contact, true) &&
                            Validator.isValidName(edt_org_address, true)
                        ) {
                            authViewModel.registerCurrentStep = OrganiserDetailsSteps.PASSWORD
                            binding.step = OrganiserDetailsSteps.PASSWORD
                            binding.stepView.go(1, true)
                            signupMap.put("email", edit_email.text.toString())
                            signupMap.put("contact", edt_org_contact.text.toString())
                            signupMap.put("address", edt_org_address.text.toString())
                        }
                    }
                }
            }
            OrganiserDetailsSteps.BANK_DETAILS -> {
                if (Validator.isValidBankAccNo(edit_bank_acc_no)
                    && Validator.isValidIFSC(edt_ifsc)
                ) {
                    authViewModel.registerCurrentStep = OrganiserDetailsSteps.PASSWORD
                    binding.step = OrganiserDetailsSteps.PASSWORD
                    binding.stepView.go(2, true)
                }
            }
            OrganiserDetailsSteps.PASSWORD -> {
                if (Validator.isValidPassword(edt_password)) {
                    if (TextUtils.equals(edt_password.text, edt_confirm_password.text)) {
                        signupMap.put("password", edt_password.text.toString())
                        if (authViewModel.userType == USER_TYPE.ORGANISER) {
                            signupMap.put("organization", edt_org_name.text.toString())
                            signupMap.put("role", ROLE.ORGANIZER.role)
                        } else {
                            signupMap.put("role", "subscriber")
                        }
                        //since default value here is 100, if its 100 then we show user tnc dialog
                        if (ModelPreferencesManager.getInt(Constants.TERMS_AND_CONDITION) == 100) {
                            TermsAndConditionDialog.openDialog(activity!!) {
                                ModelPreferencesManager.putInt(Constants.TERMS_AND_CONDITION, 1)
                                authViewModel.register(signupMap)
                            }
                        } else if (ModelPreferencesManager.getInt(Constants.TERMS_AND_CONDITION) == 1) {
                            authViewModel.register(signupMap)
                        }
                    } else {
                        showUserMsg("Password Does not match")
                    }
                }
            }
        }
    }

    private fun initViews() {
        val steps = listOf<String>(
            OrganiserDetailsSteps.BASIC_DETAILS.desc,
            OrganiserDetailsSteps.PASSWORD.desc
        )
        binding.stepView.setSteps(steps)
        binding.title.text = "Registration - ${authViewModel.userType?.desc}"
        authViewModel.registerCurrentStep = OrganiserDetailsSteps.BASIC_DETAILS
        binding.step = OrganiserDetailsSteps.BASIC_DETAILS
        binding.userType = authViewModel.userType
    }

    fun showUserMsg(msg: String) {
        Toast.makeText(activity, msg, Toast.LENGTH_LONG).show()
    }

}
