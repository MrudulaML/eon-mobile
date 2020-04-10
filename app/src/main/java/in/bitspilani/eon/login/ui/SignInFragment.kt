package `in`.bitspilani.eon.login.ui


import `in`.bitspilani.eon.BitsEonApp
import `in`.bitspilani.eon.R
import `in`.bitspilani.eon.utils.Constants
import `in`.bitspilani.eon.utils.ModelPreferencesManager
import `in`.bitspilani.eon.utils.Validator
import `in`.bitspilani.eon.utils.clickWithDebounce
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

            //save object to pref
            ModelPreferencesManager.put(it, Constants.CURRENT_USER)
            showUserMsg("Login Successful")
            findNavController().navigate(R.id.action_signInFragment_to_homeFragment)

        })

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        authViewModel = activity?.run {
            ViewModelProviders.of(this).get(AuthViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        setUpClickListeners()
        setObservables()
    }

    private fun setUpClickListeners() {
        btn_login.clickWithDebounce {
            if (authViewModel.userType != null) {
                //local validation for password email
                if (Validator.isValidEmail(etEmailAddress, true)) {
                    authViewModel.login(etEmailAddress.text.toString(), etPassword.text.toString())
                } else {

                    showUserMsg("Please select user role")
                }
            }

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
            if (authViewModel.userType != null) {
                findNavController().navigate(R.id.action_signInFragment_to_basicInfo)
            } else {
                showUserMsg("Select user type")
            }
        }
        organiser.clickWithDebounce {
            organiser.isChecked = true
            subscriber.isChecked = false
            authViewModel.userType = USER_TYPE.ORGANISER
        }
        subscriber.clickWithDebounce {
            organiser.isChecked = false
            subscriber.isChecked = true
            authViewModel.userType = USER_TYPE.SUBSCRIBER
        }

    }

    private fun setRoleToPref(role: String) {
        BitsEonApp.localStorageHandler?.user_role = role
    }

    override fun onResume() {
        super.onResume()
        actionbarHost?.showToolbar(showToolbar = false, showBottomNav = false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ActionbarHost) {
            actionbarHost = context
        }
    }

    private fun showUserMsg(msg: String) {
        Toast.makeText(activity, msg, Toast.LENGTH_LONG).show()
    }
}

interface ActionbarHost {
    fun showToolbar(showToolbar: Boolean, title: String? = null, showBottomNav: Boolean)
}
