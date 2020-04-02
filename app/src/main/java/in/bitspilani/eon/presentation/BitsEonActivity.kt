package `in`.bitspilani.eon.presentation

import `in`.bitspilani.eon.BitsEonApp
import `in`.bitspilani.eon.R
import `in`.bitspilani.eon.databinding.ActivityBitsEonBinding
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class BitsEonActivity : AppCompatActivity() {
    lateinit var navController: NavController
    lateinit var binding: ActivityBitsEonBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_bits_eon)
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        checkIfAuthenticated()
    }

    private fun checkIfAuthenticated(){
        lifecycleScope.launch {
            if (BitsEonApp.localStorageHandler!!.token.isNullOrEmpty()){

                delay(700)
                navController.navigate(R.id.action_splashScreen_to_signInFragment,
                    null,
                    NavOptions.Builder()
                        .setPopUpTo(R.id.splashScreen,
                            true).build()
                )
            }else{

                delay(700)
                navController.navigate(R.id.action_splashScreen_to_signInFragment,
                    null,
                    NavOptions.Builder()
                        .setPopUpTo(R.id.splashScreen,
                            true).build()
                )
            }
        }

    }

}
