package `in`.bitspilani.eon.presentation.auth.fragments


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
import kotlinx.android.synthetic.main.fragment_sign_in.*

class SignInFragment : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_login.clickWithDebounce {

           if( Validator.isValidName(etEmailAddress,true) &&
            Validator.isValidPassword(etPassword,true))
               Toast.makeText(activity,"Login Successful",Toast.LENGTH_LONG).show()

            //TODO map the API for login

        }

        tv_forgot_password.clickWithDebounce {

            // click event on forgot password
            //TODO map the API for forgot password
            Toast.makeText(getActivity(),"Forgot password",Toast.LENGTH_SHORT).show()
        }

        btn_register.clickWithDebounce {

            findNavController().navigate(R.id.action_signInFragment_to_basicInfo)
        }

    }
}
