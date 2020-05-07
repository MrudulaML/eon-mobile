package `in`.bitspilani.eon.login.ui


import `in`.bitspilani.eon.BitsEonActivity
import `in`.bitspilani.eon.R
import `in`.bitspilani.eon.utils.*
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_sign_in.*


class SignInFragment : Fragment() {
    lateinit var authViewModel: AuthViewModel
    private var actionbarHost: ActionbarHost? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.getWindow()?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    private fun setObservables() {
        authViewModel.loginLiveData.observe(viewLifecycleOwner, Observer {

            //save object to pref
            ModelPreferencesManager.putString(Constants.ACCESS_TOKEN, it.data.access)
            ModelPreferencesManager.putString(Constants.REFRESH_TOKEN, it.data.refresh)
            ModelPreferencesManager.putInt(Constants.USER_ROLE, it.data.user.role.id)
            ModelPreferencesManager.put(it.data, Constants.CURRENT_USER)
            view?.showSnackbar("Login Successful")
            findNavController().navigate(
                R.id.action_signInFragment_to_homeFragment,
                null,
                NavOptions.Builder()
                    .setPopUpTo(
                        R.id.app_nav,
                        true
                    ).build()
            )

        })

        authViewModel.errorView.observe(viewLifecycleOwner, Observer {

            view?.showSnackbar(it,0)

        })
        authViewModel.progressLiveData.observe(viewLifecycleOwner, Observer {

            (activity as BitsEonActivity).showProgress(it)
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

        organiser.isChecked = true
        authViewModel.userType = USER_TYPE.ORGANISER
        btn_login.clickWithDebounce {

            //local validation for password email
            if (Validator.isValidEmail(etEmailAddress, true) && Validator.isValidLoginPassword(etPassword, true)) {
                authViewModel.login(etEmailAddress.text.toString(), etPassword.text.toString())
            }
//            else {
//                view?.showSnackbar("Please select user role")
//            }
        }

        actionbarHost?.showToolbar(showToolbar = false, showBottomNav = false)


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

    override fun onResume() {
        super.onResume()
        organiser.isChecked = true
        subscriber.isChecked = false
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
