package `in`.bitspilani.eon.presentation.auth.fragments.signup

import `in`.bitspilani.eon.BitsEonApp
import `in`.bitspilani.eon.R
import `in`.bitspilani.eon.databinding.FragmentBasicDetailsBinding
import `in`.bitspilani.eon.utils.Validator
import `in`.bitspilani.eon.utils.clickWithDebounce
import `in`.bitspilani.eon.utils.getViewModelFactory
import `in`.bitspilani.eon.viewmodel.AuthViewModel
import `in`.bitspilani.eon.viewmodel.OrganiserDetailsSteps
import `in`.bitspilani.eon.viewmodel.USER_TYPE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_basic_details.*
import kotlinx.android.synthetic.main.fragment_basic_details.edt_confirm_password
import kotlinx.android.synthetic.main.fragment_basic_details.edt_password
import kotlinx.android.synthetic.main.fragment_create_password.*


class BasicDetailsFragment : Fragment() {

    lateinit var authViewModel:AuthViewModel
    lateinit var binding: FragmentBasicDetailsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_basic_details, container, false)
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
    }
    private fun onNextClick(){
        when(authViewModel.registerCurrentStep){
            OrganiserDetailsSteps.BASIC_DETAILS ->{
                if (Validator.isValidEmail(edit_email, true) &&
                    Validator.isValidPhone(edt_org_contact, true) &&
                    Validator.isValidName(edt_org_address, true)
                ){
                    if(authViewModel.userType == USER_TYPE.ORGANISER) {
                        authViewModel.registerCurrentStep = OrganiserDetailsSteps.BANK_DETAILS
                        binding.step = OrganiserDetailsSteps.BANK_DETAILS
                        binding.stepView.go(1, true)
                    }else{
                        authViewModel.registerCurrentStep = OrganiserDetailsSteps.PASSWORD
                        binding.step = OrganiserDetailsSteps.PASSWORD
                        binding.stepView.go(1, true)
                    }
                }

            }

            OrganiserDetailsSteps.BANK_DETAILS->{
                if (Validator.isValidBankAccNo(edit_bank_acc_no)
                    && Validator.isValidIFSC(edt_ifsc)){
                    authViewModel.registerCurrentStep = OrganiserDetailsSteps.PASSWORD
                    binding.step = OrganiserDetailsSteps.PASSWORD
                    binding.stepView.go(2,true)
                }
            }

            OrganiserDetailsSteps.PASSWORD->{
                if (Validator.isValidPassword(edt_password)
                    && Validator.isValidPassword(edt_confirm_password)){

                        BitsEonApp.localStorageHandler?.token = "abcdefg" //dummy token to mock auth
                        findNavController().navigate(R.id.action_basicInfo_to_homeFragment,
                            null,
                            NavOptions.Builder()
                                .setPopUpTo(R.id.basicInfo,
                                    true).build()
                        )
                }
            }
        }
    }
    private fun initViews(){
        if (authViewModel.userType==USER_TYPE.ORGANISER){
            val steps = listOf<String>(
                OrganiserDetailsSteps.BASIC_DETAILS.desc,
                OrganiserDetailsSteps.BANK_DETAILS.desc,
                OrganiserDetailsSteps.PASSWORD.desc)
            binding.stepView.setSteps(steps)
        }else{
            val steps = listOf<String>(
                OrganiserDetailsSteps.BASIC_DETAILS.desc,
                OrganiserDetailsSteps.PASSWORD.desc)
            binding.stepView.setSteps(steps)
        }

        authViewModel.registerCurrentStep = OrganiserDetailsSteps.BASIC_DETAILS
        binding.step = OrganiserDetailsSteps.BASIC_DETAILS
        binding.userType = authViewModel.userType
    }

}
