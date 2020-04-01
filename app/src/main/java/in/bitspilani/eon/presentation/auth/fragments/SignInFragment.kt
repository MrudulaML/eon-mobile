package `in`.bitspilani.eon.presentation.auth.fragments


import `in`.bitspilani.eon.R
import `in`.bitspilani.eon.utils.getViewModelFactory
import `in`.bitspilani.eon.viewmodel.AuthViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels


class SignInFragment : Fragment() {

    private val authViewModel by viewModels<AuthViewModel> { getViewModelFactory() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }


}
