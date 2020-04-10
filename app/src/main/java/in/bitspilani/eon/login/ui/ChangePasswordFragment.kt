package `in`.bitspilani.eon.login.ui

import `in`.bitspilani.eon.R
import `in`.bitspilani.eon.eventOrganiser.data.Invitee
import `in`.bitspilani.eon.eventOrganiser.ui.CallbackListener
import `in`.bitspilani.eon.eventOrganiser.ui.EventDashboardViewModel
import `in`.bitspilani.eon.utils.MarginItemDecoration
import `in`.bitspilani.eon.utils.Validator
import `in`.bitspilani.eon.utils.clickWithDebounce
import `in`.bitspilani.eon.utils.getViewModelFactory
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_add_invitee.*
import kotlinx.android.synthetic.main.fragment_add_invitee.btn_close
import kotlinx.android.synthetic.main.fragment_change_password.*
import kotlinx.android.synthetic.main.fragment_invitee.*


/**
 * A simple [Fragment] subclass.
 *
 */
class ChangePasswordFragment(private val callbackListener: CallbackListener) : DialogFragment() {

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
        actionbarHost?.showToolbar(showToolbar = true, showBottomNav = true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*     button.setOnClickListener {
                 //send back data to PARENT fragment using callback
                 callbackListener.onDataReceived(editText.text.toString())
                 // Now dismiss the fragment
                 dismiss()*/



        buttonClick()

    }


    fun buttonClick() {

        btn_close.clickWithDebounce { dismiss() }
        btn_password_cancel.clickWithDebounce { dismiss() }

        btn_password_confirm.clickWithDebounce {
            if (Validator.isValidPassword(edt_create_password)) {
                if (TextUtils.equals(edt_create_password.text, edt_confirm_password.text)) {


                    var hashMap = HashMap<String, Any>()

                    //  hashMap.put("email",)
                    hashMap.put("old_password", edt_current_password.text.toString())
                    hashMap.put("new_password", edt_create_password.text.toString())

                    changePwViewmodel.changePasswordMsg
                }

            }
        }
    }


}

