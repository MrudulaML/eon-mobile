package `in`.bitspilani.eon


import `in`.bitspilani.eon.login.ui.AuthViewModel
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class BitsEonActivity : AppCompatActivity() {
    lateinit var navController: NavController
    lateinit var authViewModel: AuthViewModel
    lateinit var bottomNavigation : BottomNavigationView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bits_eon)
        authViewModel = ViewModelProviders.of(this).get(AuthViewModel::class.java)
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        bottomNavigation= findViewById(R.id.bottom_navigation)
        checkIfAuthenticated()

        NavigationUI.setupWithNavController(bottomNavigation,navController)

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun checkIfAuthenticated(){
        lifecycleScope.launch {
            if (false){

                delay(700)
                navController.navigate(R.id.action_splashScreen_to_signInFragment,
                    null,
                    NavOptions.Builder()
                        .setPopUpTo(R.id.splashScreen,
                            true).build()
                )
            }else{

                delay(700)
                navController.navigate(R.id.action_splashScreen_to_homeFragment,
                    null,
                    NavOptions.Builder()
                        .setPopUpTo(R.id.splashScreen,
                            true).build()
                )
            }
        }

    }



}
