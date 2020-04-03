package `in`.bitspilani.eon.presentation.auth


import `in`.bitspilani.eon.R
import `in`.bitspilani.eon.utils.Validator
import `in`.bitspilani.eon.utils.clickWithDebounce
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_change_password.*
import kotlinx.android.synthetic.main.fragment_sign_in.*

class ChangePasswordFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_change_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_submit.clickWithDebounce {

            if (Validator.isValidPassword(edt_password, true)
                && Validator.isValidPassword(edt_confirm_password,true)
            ) {
                if (!edt_password.text.toString().equals(edt_confirm_password.text.toString())){
                    Toast.makeText(activity, "Password Should match", Toast.LENGTH_LONG).show()

                }
                else {
                    Toast.makeText(activity, "Password Change Successfully", Toast.LENGTH_LONG)
                        .show()
                    findNavController().navigate(R.id.signInFragment)

                }
            }
        }


    }
}
