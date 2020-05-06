package `in`.bitspilani.eon


import `in`.bitspilani.eon.api.ApiService
import `in`.bitspilani.eon.api.RestClient
import `in`.bitspilani.eon.event_organiser.models.FilterResponse
import `in`.bitspilani.eon.event_organiser.viewmodel.EventDashboardViewModel
import `in`.bitspilani.eon.event_organiser.viewmodel.NotificationViewModel
import `in`.bitspilani.eon.login.data.Data
import `in`.bitspilani.eon.login.ui.ActionbarHost
import `in`.bitspilani.eon.utils.*
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.util.Base64
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.postDelayed
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.auth0.android.jwt.JWT
import com.facebook.FacebookSdk
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_bits_eon.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class BitsEonActivity : AppCompatActivity(), ActionbarHost {

    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FacebookSdk.sdkInitialize(this.applicationContext)
        setContentView(R.layout.activity_bits_eon)

        navController = Navigation.findNavController(this, R.id.nav_host_fragment)

        setSupportActionBar(toolbar)
        showToolbar(showToolbar = false, showBottomNav = false)


        checkIfAuthenticated()
        NavigationUI.setupWithNavController(bottom_navigation, navController)


    }



    private fun checkIfAuthenticated() {
        lifecycleScope.launch {
            val userData = ModelPreferencesManager.get<Data>(Constants.CURRENT_USER)
            when {
                userData?.access.isNullOrEmpty() -> {
                    delay(3000)
                    navController.navigate(R.id.action_splashScreen_to_signInFragment)

                }
                JWT(userData!!.access).isExpired(10) -> {
                    delay(400)
                    ModelPreferencesManager.clearCache()
                    Toast.makeText(this@BitsEonActivity, "Session expired", Toast.LENGTH_LONG)
                        .show()
                    navController.navigate(R.id.action_splashScreen_to_signInFragment)

                }
                else -> {
                    delay(400)
                    navController.navigate(R.id.action_splashScreen_to_HomeFragment)

                }
            }
        }

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {



        return when (item.itemId) {
            R.id.userProfileFragment -> {

                navController.navigate(R.id.nav_user_profile)

                true
            }
            R.id.notificationFragment -> {
                navController.navigate(R.id.notificationFragment)
                return true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun showToolbar(showToolbar: Boolean, title: String?, showBottomNav: Boolean) {
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
    /*  private var backPressedOnce = false
      override fun onBackPressed() {
          if (navController.graph.startDestination == navController.currentDestination?.id)
          {
              if (backPressedOnce)
              {
                  super.onBackPressed()
                  return
              }

              backPressedOnce = true
              Toast.makeText(this, "Press BACK again to exit", Toast.LENGTH_SHORT).show()

              Handler().postDelayed(2000) {
                  backPressedOnce = false
              }
          }
          else {
              super.onBackPressed()
          }
      }*/

    fun showProgress(show: Boolean) = progress.goneUnless(visible = show)

}
