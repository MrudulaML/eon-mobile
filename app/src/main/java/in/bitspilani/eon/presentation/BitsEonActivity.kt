package `in`.bitspilani.eon.presentation

import `in`.bitspilani.eon.BitsEonApp
import `in`.bitspilani.eon.R
import `in`.bitspilani.eon.databinding.ActivityBitsEonBinding
import `in`.bitspilani.eon.viewmodel.AuthViewModel
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_bits_eon.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class BitsEonActivity : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener {
    lateinit var navController: NavController
    lateinit var binding: ActivityBitsEonBinding
    lateinit var authViewModel:AuthViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_bits_eon)
        binding.toolbar.visibility = View.GONE
        authViewModel = ViewModelProviders.of(this).get(AuthViewModel::class.java)
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        checkIfAuthenticated()
        setNavigationViewListener()

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
                navController.navigate(R.id.action_splashScreen_to_homeFragment,
                    null,
                    NavOptions.Builder()
                        .setPopUpTo(R.id.splashScreen,
                            true).build()
                )
            }
        }

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {


        when(item.itemId){

            R.id.action_change_password ->{

                navController.navigate(R.id.changePasswordFragment)
                drawer_layout.closeDrawer(GravityCompat.START)
            }
            R.id.action_logout->{

                navController.navigate(R.id.homeFragment)
                drawer_layout.closeDrawer(GravityCompat.START)
            }

        }
        return true
    }
    private fun setNavigationViewListener() {
        val navigationView =
            findViewById<View>(R.id.nav_drawer) as NavigationView
        navigationView.setNavigationItemSelectedListener(this)
    }

}
