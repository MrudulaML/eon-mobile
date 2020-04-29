package `in`.bitspilani.eon.login.ui

import `in`.bitspilani.eon.BitsEonActivity
import `in`.bitspilani.eon.R
import `in`.bitspilani.eon.login.data.Data
import `in`.bitspilani.eon.utils.*
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_add_invitee.btn_close
import kotlinx.android.synthetic.main.fragment_change_password.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber


/**
 * A simple [Fragment] subclass.
 *
 */
class ChangePasswordFragment() : DialogFragment() {

    private val changePwViewmodel by viewModels<ChangePwViewModel> { getViewModelFactory() }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isCancelable = false
        return inflater.inflate(R.layout.fragment_change_password, container, false)
    }

    override fun getTheme(): Int {
        return R.style.DialogTheme
    }

    /**
     * toggle visibility of different navigation
     */
    private var actionbarHost: ActionbarHost? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ActionbarHost) {
            actionbarHost = context
        }
    }

    override fun onDetach() {
        super.onDetach()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*     button.setOnClickListener {
                 //send back data to PARENT fragment using callback
                 callbackListener.onDataReceived(editText.text.toString())
                 // Now dismiss the fragment
                 dismiss()*/

        buttonClick()
        setObservables()


    }

    fun setObservables() {

        changePwViewmodel.changePasswordMsg.observe(viewLifecycleOwner, Observer {
            if (it != null) {

                showUserMsg(it)

//                findNavController().popBackStack(R.id.changePasswordFragment,true)
//
//                findNavController().navigate(R.id.signInFragment)

                ModelPreferencesManager.clearCache()
                Timber.d("Cached cleared")
                (activity as BitsEonActivity).showProgress(true)
                lifecycleScope.launch {
                    delay(400)
                    ModelPreferencesManager.clearCache()
                    Timber.d("Cached cleared")
                    (activity as BitsEonActivity).showProgress(true)
                    lifecycleScope.launch {
                        delay(400)

                        findNavController().navigate(R.id.signInFragment,
                            null,
                            NavOptions.Builder()
                                .setPopUpTo(R.id.app_nav,
                                    true)
                                .build())
                        actionbarHost?.showToolbar(showToolbar = false, showBottomNav = false)
                        (activity as BitsEonActivity).showProgress(false)

                }

            }
            }
        })

        changePwViewmodel.errorToast.observe(viewLifecycleOwner, Observer {

            if (it != null) {

                showUserMsg(it)
            }
        })

        changePwViewmodel.progressLiveData.observe(viewLifecycleOwner, Observer {

            (activity as BitsEonActivity).showProgress(it)
        })

    }

    fun showUserMsg(msg: String) {
        Toast.makeText(activity, msg, Toast.LENGTH_LONG).show()
    }

    fun buttonClick() {

        btn_close.clickWithDebounce { dismiss() }
        btn_password_cancel.clickWithDebounce { dismiss() }

        btn_password_confirm.clickWithDebounce {
            if (Validator.isValidPassword(edt_current_password, true) &&
                Validator.isValidPassword(edt_create_password, true) &&
                Validator.isValidPassword(edt_confirm_password, true)
            ) {
                if (TextUtils.equals(edt_create_password.text, edt_confirm_password.text)) {

                    var hashMap = HashMap<String, Any>()

                    val userData = ModelPreferencesManager.get<Data>(Constants.CURRENT_USER)

                    hashMap.put("email", userData?.user?.email as Any)
                    hashMap.put("old_password", edt_current_password.text.toString())
                    hashMap.put("new_password", edt_create_password.text.toString())

                    changePwViewmodel.changePassword(hashMap)
                } else {
                    showUserMsg("Password not matched")
                }

            } else {
                showUserMsg("Please input your password!")
            }
        }

        setData()
    }

    private fun setData() {
        edt_basic_email.isEnabled = false
        val userData = ModelPreferencesManager.get<Data>(Constants.CURRENT_USER)
        edt_basic_email.setText(userData?.user?.email, TextView.BufferType.EDITABLE)
    }
}

