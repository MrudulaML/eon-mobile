package `in`.bitspilani.eon.login.ui

import `in`.bitspilani.eon.BitsEonActivity
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
import com.shuhart.stepview.StepView
import kotlinx.android.synthetic.main.fragment_basic_details.*




class BasicDetailsFragment : Fragment() {

    lateinit var stepView : StepView

    var isBack = false

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

    override fun onDetach() {
        super.onDetach()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        authViewModel = activity?.run {
            ViewModelProviders.of(this).get(AuthViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        initViews()

        btn_reg_back.clickWithDebounce {
            onBackClick()
        }
        btn_next.clickWithDebounce {
            onNextClick()
        }

        setObservers()
    }


    fun setObservers() {

        authViewModel.registerData.observe(viewLifecycleOwner, Observer {

            showUserMsg("Registration Successful")

            if (it.user.role.id == 2) {

                findNavController().navigate(R.id.action_BasicInfoFragment_to_signInFragment)

            } else {

                ContactAdmin.openDialog(activity!!) {

                    findNavController().navigate(R.id.action_BasicInfoFragment_to_signInFragment)
                }
            }


        })

        authViewModel.registerError.observe(viewLifecycleOwner, Observer {

            showUserMsg(it)
        })

        authViewModel.progressLiveData.observe(viewLifecycleOwner, Observer {

            (activity as BitsEonActivity).showProgress(it)
        })

        authViewModel.errorView.observe(viewLifecycleOwner, Observer {

            showUserMsg(it)
        })
    }

    private fun onBackClick(){

        if (isBack){
            isBack = false

            authViewModel.registerCurrentStep= OrganiserDetailsSteps.BASIC_DETAILS
            binding.step = OrganiserDetailsSteps.BASIC_DETAILS

            binding.stepView.go(0, true)

        }else{
            findNavController().popBackStack()  //  <- this code is to take him back to login screen
        }
    }

    private fun onNextClick() {
        when (authViewModel.registerCurrentStep) {
            OrganiserDetailsSteps.BASIC_DETAILS -> {

                isBack = true

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
//                isBack = true
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
                        showUserMsg("Password doesn't match")
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
