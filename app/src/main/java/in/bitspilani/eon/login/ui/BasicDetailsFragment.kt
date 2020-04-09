package `in`.bitspilani.eon.login.ui

import `in`.bitspilani.eon.BitsEonApp
import `in`.bitspilani.eon.R
import `in`.bitspilani.eon.databinding.FragmentBasicDetailsBinding
import `in`.bitspilani.eon.utils.Validator
import `in`.bitspilani.eon.utils.clickWithDebounce
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavOptions
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


        authViewModel.registerData.observe(this, Observer {

            showUserMsg("Registration Successful")

            findNavController().navigate(R.id.action_BasicInfoFragment_to_homeFragment)

        })
        authViewModel.registerError.observe(this, Observer {

        })
    }

    private fun onNextClick() {
        when (authViewModel.registerCurrentStep) {
            OrganiserDetailsSteps.BASIC_DETAILS -> {
                if (Validator.isValidEmail(edit_email, true) &&
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
                        BitsEonApp.localStorageHandler?.token = "abcdefg" //dummy token to mock auth

                        signupMap.put("password", edt_password.text.toString())
                        if (authViewModel.userType == USER_TYPE.ORGANISER) {

                            signupMap.put("edt_org_name", edt_org_name.text.toString())

                            BitsEonApp.localStorageHandler?.user_role = "organiser"
                            signupMap.put("role", "organiser")

                        } else {
                            BitsEonApp.localStorageHandler?.user_role = "subscriber"
                            signupMap.put("role", "subscriber")

                        }

                        authViewModel.register(signupMap)


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
