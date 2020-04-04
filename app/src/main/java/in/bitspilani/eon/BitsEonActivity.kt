package `in`.bitspilani.eon

import `in`.bitspilani.eon.databinding.ActivityBitsEonBinding
import `in`.bitspilani.eon.login.ui.AuthViewModel
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_bits_eon.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class BitsEonActivity : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener {
    lateinit var navController: NavController
    lateinit var binding: ActivityBitsEonBinding
    lateinit var authViewModel: AuthViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_bits_eon)
        binding.toolbar.visibility = View.GONE
        authViewModel = ViewModelProviders.of(this).get(AuthViewModel::class.java)
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        checkIfAuthenticated()
        setNavigationViewListener()
        binding.menu.setOnClickListener {
            binding.drawerLayout.openDrawer(Gravity.LEFT)
        }
//        binding.layoutDrawer.logout.setOnClickListener {
//            binding.drawerLayout.closeDrawer(Gravity.LEFT)
//            binding.toolbar.visibility = View.GONE
//            BitsEonApp.localStorageHandler?.clearData()
//            navController.navigate(R.id.action_global_signInFragment,
//                null,
//                NavOptions.Builder()
//                    .setPopUpTo(R.id.signInFragment,
//                        true).build()
//            )
//
//        }
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
                navController.navigate(R.id.action_to_change_password)
                drawer_layout.closeDrawer(GravityCompat.START)
            }
            R.id.action_logout->{
                binding.toolbar.visibility = View.GONE
                drawer_layout.closeDrawer(GravityCompat.START)
                BitsEonApp.localStorageHandler?.clearData()
                navController.navigate(R.id.action_global_signInFragment,
                    null,
                    NavOptions.Builder()
                        .setPopUpTo(R.id.signInFragment,
                            true).build()
                )
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