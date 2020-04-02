package `in`.bitspilani.eon.presentation.auth.fragments


import `in`.bitspilani.eon.R
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class SignInFragment : Fragment() {

    lateinit var username: EditText
    lateinit var password: EditText
    lateinit var buttonLogin: Button
    lateinit var forgotPassword: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        username = view.findViewById(R.id.etEmailAddress)
        password = view.findViewById(R.id.etPassword)
        buttonLogin = view.findViewById(R.id.btn_login)
        forgotPassword = view.findViewById(R.id.tv_forgot_password)

        buttonLogin.setOnClickListener(View.OnClickListener {

            // getting input username and password
            val username = username.text.toString().trim();
            val password = password.text.toString().trim();

            // to do validation

            // demo of getting data from user - need to remove
            Toast.makeText(getActivity(),"Username: "+username+"Password: "+password,Toast.LENGTH_SHORT).show();
        })

        forgotPassword.setOnClickListener(View.OnClickListener {

            // to do

            // click event on forgot password
            Toast.makeText(getActivity(),"Forgot password",Toast.LENGTH_SHORT).show();
        })

    }
}
