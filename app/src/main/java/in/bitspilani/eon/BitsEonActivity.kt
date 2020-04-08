package `in`.bitspilani.eon


import `in`.bitspilani.eon.login.ui.ActionbarHost
import `in`.bitspilani.eon.login.ui.AuthViewModel
import `in`.bitspilani.eon.utils.goneUnless
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_bits_eon.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class BitsEonActivity : AppCompatActivity(),ActionbarHost {
    lateinit var navController: NavController
    lateinit var authViewModel: AuthViewModel
    lateinit var bottomNavigation : BottomNavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bits_eon)
        authViewModel = ViewModelProviders.of(this).get(AuthViewModel::class.java)
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        bottomNavigation= findViewById(R.id.bottom_navigation)
        setSupportActionBar(toolbar)
        supportActionBar!!.hide()
        bottom_navigation.visibility=View.GONE
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        checkIfAuthenticated()
        NavigationUI.setupWithNavController(bottomNavigation,navController)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.top_navigation, menu)
        return true
    }


    private fun checkIfAuthenticated(){
        lifecycleScope.launch {

            if (BitsEonApp.localStorageHandler?.token ==null){

                delay(200)
                navController.navigate(R.id.action_splashScreen_to_signInFragment,
                    null,
                    NavOptions.Builder()
                        .setPopUpTo(R.id.splashScreen,
                            true).build()
                )
            }else{

                delay(200)
                navController.navigate(R.id.action_splashScreen_to_homeFragment,
                    null,
                    NavOptions.Builder()
                        .setPopUpTo(R.id.splashScreen,
                            true).build()
                )
            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.userProfileFragment -> {

                navController.navigate(R.id.userProfileFragment)

                true
            }
            R.id.notificationFragment ->{
                navController.navigate(R.id.notificationFragment)

                return true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun showToolbar(showToolbar: Boolean, title: String?,showBottomNav : Boolean) {
        if (supportActionBar != null) {
            if (showToolbar) {
                supportActionBar!!.show()
            } else {
                supportActionBar!!.hide()
            }
        }
        title?.let {
            supportActionBar!!.title = title
        }

        bottom_navigation.goneUnless(showBottomNav)

    }

}
