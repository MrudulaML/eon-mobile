package `in`.bitspilani.eon


import `in`.bitspilani.eon.api.ApiService
import `in`.bitspilani.eon.api.RestClient
import `in`.bitspilani.eon.event_organiser.models.FilterResponse
import `in`.bitspilani.eon.login.data.Data
import `in`.bitspilani.eon.login.data.LoginResponse
import `in`.bitspilani.eon.login.data.User
import `in`.bitspilani.eon.login.ui.ActionbarHost
import `in`.bitspilani.eon.login.ui.AuthViewModel
import `in`.bitspilani.eon.utils.ApiCallback
import `in`.bitspilani.eon.utils.Constants
import `in`.bitspilani.eon.utils.ModelPreferencesManager
import `in`.bitspilani.eon.utils.goneUnless
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.auth0.android.jwt.JWT
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_bits_eon.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class BitsEonActivity : AppCompatActivity(),ActionbarHost {
    lateinit var navController: NavController
    lateinit var bottomNavigation : BottomNavigationView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bits_eon)
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        bottomNavigation= findViewById(R.id.bottom_navigation)
        setSupportActionBar(toolbar)
        supportActionBar!!.hide()
        bottom_navigation.visibility=View.GONE
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        setupEventTypes()
        checkIfAuthenticated()
        NavigationUI.setupWithNavController(bottomNavigation,navController)

    }

    //TODO fix this hack replace this with rx
    private fun setupEventTypes() {
       RestClient().authClient.create(ApiService::class.java).getFilter()
            .enqueue(object : ApiCallback<FilterResponse>(){
                override fun onSuccessResponse(responseBody: FilterResponse) {

                    ModelPreferencesManager.put(responseBody, Constants.EVENT_TYPES)

                }

                override fun onApiError(errorType: ApiError, error: String?) {

                }
            })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.top_navigation, menu)
        return true
    }


    private fun checkIfAuthenticated(){
        lifecycleScope.launch {
            val userData = ModelPreferencesManager.get<Data>(Constants.CURRENT_USER)
            when {
                userData?.access.isNullOrEmpty() -> {
                    delay(400)
                    navController.navigate(R.id.action_splashScreen_to_signInFragment,
                        null,
                        NavOptions.Builder()
                            .setPopUpTo(R.id.splashScreen,
                                true).build())

                    //TODO fix this hack put null safety prone to crash
                }
                JWT(userData!!.access).isExpired(10) -> {
                    delay(400)
                    ModelPreferencesManager.clearCache()
                    Toast.makeText(this@BitsEonActivity, "Session expired", Toast.LENGTH_LONG).show()
                    navController.navigate(R.id.action_splashScreen_to_signInFragment,
                        null,
                        NavOptions.Builder()
                            .setPopUpTo(R.id.splashScreen,
                                true).build())

                }
                else -> {
                    delay(400)
                    navController.navigate(R.id.action_splashScreen_to_HomeFragment,
                        null,
                        NavOptions.Builder()
                            .setPopUpTo(R.id.app_nav,
                                true).build())

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

    fun showProgress(show: Boolean) = progress.goneUnless(visible = show)


}
