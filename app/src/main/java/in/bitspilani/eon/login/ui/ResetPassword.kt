package `in`.bitspilani.eon.login.ui

import `in`.bitspilani.eon.R
import `in`.bitspilani.eon.databinding.FragmentCreatePasswordBinding
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
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_create_password.*


class ResetPassword : Fragment() {

    lateinit var authViewModel: AuthViewModel
    lateinit var binding: FragmentCreatePasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

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
    }

    private fun initViews(){
        val steps = listOf<String>(
            ForgotPasswordSteps.ENTER_DETAILS.desc,
            ForgotPasswordSteps.VERIFICATION_CODE.desc,
            ForgotPasswordSteps.PASSWORD.desc,
            ForgotPasswordSteps.SUCCESS.desc
            )
        binding.stepView.setSteps(steps)
        binding.title.text = "Forgot Password"
        authViewModel.forgotPasswordSteps = ForgotPasswordSteps.ENTER_DETAILS
        binding.step = ForgotPasswordSteps.ENTER_DETAILS
    }

    private fun onNextClick(){
        when(authViewModel.forgotPasswordSteps){
            ForgotPasswordSteps.ENTER_DETAILS ->{
                if (Validator.isValidEmail(edit_user_email, true)
                ){
                    authViewModel.forgotPasswordSteps = ForgotPasswordSteps.VERIFICATION_CODE
                    binding.step = ForgotPasswordSteps.VERIFICATION_CODE
                    binding.stepView.go(1, true)
                }

            }

            ForgotPasswordSteps.VERIFICATION_CODE->{
                if (Validator.isValidVerificationCode(edit_verification_code)){
                    authViewModel.forgotPasswordSteps = ForgotPasswordSteps.PASSWORD
                    binding.step = ForgotPasswordSteps.PASSWORD
                    binding.stepView.go(2,true)
                }
            }

            ForgotPasswordSteps.PASSWORD->{
                if (Validator.isValidPassword(edt_create_new_password)){
                    if(TextUtils.equals(edt_create_new_password.text,edt_re_enter_password.text)) {
                        authViewModel.forgotPasswordSteps = ForgotPasswordSteps.SUCCESS
                        binding.step = ForgotPasswordSteps.SUCCESS
                        binding.stepView.go(3,true)
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
