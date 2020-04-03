package `in`.bitspilani.eon.presentation.auth.fragments


import `in`.bitspilani.eon.BitsEonApp
import `in`.bitspilani.eon.R
import `in`.bitspilani.eon.utils.Validator
import `in`.bitspilani.eon.utils.clickWithDebounce
import `in`.bitspilani.eon.utils.getViewModelFactory
import `in`.bitspilani.eon.viewmodel.AuthViewModel
import `in`.bitspilani.eon.viewmodel.USER_TYPE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_sign_in.*

class SignInFragment : Fragment() {
    lateinit var authViewModel:AuthViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        authViewModel = activity?.run {
            ViewModelProviders.of(this).get(AuthViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
        authViewModel.userType = null
        btn_login.clickWithDebounce {
            if (authViewModel.userType!=null){
                if( Validator.isValidEmail(etEmailAddress,true)){
                    BitsEonApp.localStorageHandler?.token = "abcdefg" //dummy token to mock auth
                    showUserMsg("Login Successful")
                    findNavController().navigate(R.id.action_signInFragment_to_homeFragment)
                }
            }else{
                showUserMsg("Select user type")
            }

            //TODO map the API for login

        }


        btn_register.clickWithDebounce {
            if (authViewModel.userType!=null){
                findNavController().navigate(R.id.action_signInFragment_to_basicInfo)
            }else{
                showUserMsg("Select user type")
            }
        }
        organiser.clickWithDebounce {
            organiser.isChecked = true
            guest.isChecked = false
            authViewModel.userType = USER_TYPE.ORGANISER
        }
        guest.clickWithDebounce {
            organiser.isChecked = false
            guest.isChecked = true
            authViewModel.userType = USER_TYPE.SUBSCRIBER
        }

    }
    fun showUserMsg(msg:String){
        Toast.makeText(activity,msg,Toast.LENGTH_LONG).show()
    }
}
