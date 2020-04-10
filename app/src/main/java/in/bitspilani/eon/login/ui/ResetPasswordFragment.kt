package `in`.bitspilani.eon.login.ui

import `in`.bitspilani.eon.BitsEonApp
import `in`.bitspilani.eon.R
import `in`.bitspilani.eon.databinding.FragmentCreatePasswordBinding
import `in`.bitspilani.eon.login.data.User
import `in`.bitspilani.eon.utils.Constants
import `in`.bitspilani.eon.utils.ModelPreferencesManager
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
import kotlinx.android.synthetic.main.fragment_create_password.*


class ResetPasswordFragment : Fragment() {

    lateinit var authViewModel: AuthViewModel
    lateinit var binding: FragmentCreatePasswordBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_create_password, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        authViewModel = activity?.run {
            ViewModelProviders.of(this).get(AuthViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        initViews()
        btn_forgot_password_next.clickWithDebounce {
            onNextClick()
        }
        //setUpObservables()
    }

    private fun setUpObservables() {
        authViewModel.generateCodeLiveData.observe(viewLifecycleOwner, Observer {

            binding.step = ForgotPasswordSteps.VERIFICATION_CODE
            binding.stepView.go(1, true)

        })
        authViewModel.resetPasswordLiveData.observe(viewLifecycleOwner, Observer {

           /* authViewModel.forgotPasswordSteps = ForgotPasswordSteps.PASSWORD
            binding.step = ForgotPasswordSteps.PASSWORD
            binding.stepView.go(2,true)*/
            findNavController().navigate(R.id.action_signInFragment_to_createPasswordFragment)

        })
    }

    private fun initViews(){
        val steps = listOf<String>(
            ForgotPasswordSteps.ENTER_DETAILS.desc,
            ForgotPasswordSteps.VERIFICATION_CODE.desc,
            ForgotPasswordSteps.PASSWORD.desc
            )
        binding.stepView.setSteps(steps)
        binding.title.text = "Forgot Password"
        authViewModel.forgotPasswordSteps = ForgotPasswordSteps.ENTER_DETAILS
        binding.step = ForgotPasswordSteps.ENTER_DETAILS
    }

    private fun onNextClick(){
        when(authViewModel.forgotPasswordSteps){
            ForgotPasswordSteps.ENTER_DETAILS ->{
                if (Validator.isValidEmail(edit_user_email, true)){
                    authViewModel.forgotPasswordSteps = ForgotPasswordSteps.VERIFICATION_CODE
                    authViewModel.generateCode(edit_user_email.text.toString())
                }
            }

            ForgotPasswordSteps.VERIFICATION_CODE->{
                if (Validator.isValidVerificationCode(edit_verification_code)){

                }
            }

            ForgotPasswordSteps.PASSWORD->{
                if (Validator.isValidPassword(edt_create_new_password)){
                    if(TextUtils.equals(edt_create_new_password.text,edt_re_enter_password.text)) {

                    /*    BitsEonApp.localStorageHandler?.user_email?.let {
                        authViewModel.resetPassword(it,edit_verification_code.text.toString(),edt_create_new_password.text.toString())
                        }*/

                        findNavController().navigate(R.id.action_basicInfo_to_signInFragment,
                            null,
                            NavOptions.Builder()
                                .setPopUpTo(R.id.signInFragment,
                                    true).build())
                    }else{
                        showUserMsg("Password does not match")
                    }
                }
            }
        }
    }

    fun showUserMsg(msg:String){
        Toast.makeText(activity,msg, Toast.LENGTH_LONG).show()
    }
}
