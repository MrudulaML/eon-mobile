package `in`.bitspilani.eon.login.ui


import `in`.bitspilani.eon.BitsEonApp
import `in`.bitspilani.eon.R
import `in`.bitspilani.eon.login.data.LoginResponse
import `in`.bitspilani.eon.utils.Validator
import `in`.bitspilani.eon.utils.clickWithDebounce
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
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

        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    private fun setObservables() {
        authViewModel.loginLiveData.observe(viewLifecycleOwner, Observer {
            saveUserData(it)

            showUserMsg("Login Successful")
            findNavController().navigate(R.id.action_signInFragment_to_homeFragment)

        })

    }

    private fun saveUserData(it: LoginResponse) {
        BitsEonApp.localStorageHandler?.token=it.data.access
        BitsEonApp.localStorageHandler?.user_role=it.data.user.role.role
        BitsEonApp.localStorageHandler?.user_email=it.data.user.email
        BitsEonApp.localStorageHandler?.user_id=it.data.user.user_id.toString()
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
                    //store user role

                    authViewModel.login(etEmailAddress.toString(),etPassword.toString())
                }
            }else{
                showUserMsg("Select user type")
            }

        }

        actionbarHost?.showToolbar(showToolbar = false,showBottomNav = false)
        tv_forgot_password.clickWithDebounce {

            findNavController().navigate(R.id.action_signInFragment_to_createPasswordFragment)

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
        setObservables()
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
