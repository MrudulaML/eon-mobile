package `in`.bitspilani.eon.login.ui


import `in`.bitspilani.eon.BitsEonApp
import `in`.bitspilani.eon.R
import `in`.bitspilani.eon.utils.Validator
import `in`.bitspilani.eon.utils.clickWithDebounce
import android.content.Context
import android.graphics.Outline
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_sign_in.*


class SignInFragment : Fragment() {
    lateinit var authViewModel: AuthViewModel
    private var actionbarHost: ActionbarHost? = null


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

        actionbarHost?.showToolbar(showToolbar = false,showBottomNav = false)


        tv_forgot_password.clickWithDebounce {
            findNavController().navigate(R.id.action_signInFragment_to_createPasswordFragment)
//            if (authViewModel.userType!=null){
//                findNavController().navigate(R.id.action_signInFragment_to_createPasswordFragment)
//            }else{
//                showUserMsg("Select user type")
//            }
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



    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ActionbarHost) {
            actionbarHost = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        actionbarHost?.showToolbar(showToolbar = true,showBottomNav = true)
    }

    private fun showUserMsg(msg:String){
        Toast.makeText(activity,msg,Toast.LENGTH_LONG).show()
    }
}

interface ActionbarHost {
    fun showToolbar(showToolbar: Boolean,title: String? = null,showBottomNav : Boolean)
}
