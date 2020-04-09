package `in`.bitspilani.eon.eventOrganiser.ui

import `in`.bitspilani.eon.R
import `in`.bitspilani.eon.eventOrganiser.data.Invitee
import `in`.bitspilani.eon.utils.MarginItemDecoration
import `in`.bitspilani.eon.utils.clickWithDebounce
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_add_invitee.*
import kotlinx.android.synthetic.main.fragment_invitee.*


/**
 * A simple [Fragment] subclass.
 *
 */
class AddInviteeFragment(private val callbackListener: CallbackListener) : DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isCancelable = false
        return inflater.inflate(R.layout.fragment_add_invitee, container, false)
    }
    override fun getTheme(): Int {
        return R.style.DialogTheme
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
   /*     button.setOnClickListener {
            //send back data to PARENT fragment using callback
            callbackListener.onDataReceived(editText.text.toString())
            // Now dismiss the fragment
            dismiss()*/
        btn_close.clickWithDebounce { dismiss() }
        btn_invitee_cancel.clickWithDebounce {    dismiss() }
        btn_invitee_confirm.clickWithDebounce {

            if(!TextUtils.isEmpty(edt_updated_fees.text) && !TextUtils.isEmpty(edt_email_addresses.text)
                && !TextUtils.isEmpty(edt_discount.text))
            {

                dismiss()
                callbackListener.onDataReceived("abc")
            }else{

                Toast.makeText(activity, "Please enter valid details", Toast.LENGTH_LONG).show()

            }



        }



        }


}

interface CallbackListener {
    fun onDataReceived(data: String)
}
