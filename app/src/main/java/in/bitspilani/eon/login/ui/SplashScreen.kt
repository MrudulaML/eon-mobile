package `in`.bitspilani.eon.login.ui


import `in`.bitspilani.eon.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment


class SplashScreen : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //TODO check this and remove from all the fragments if not necessary


        return inflater.inflate(R.layout.fragment_splash_screen, container, false)
    }


}
