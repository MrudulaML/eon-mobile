package `in`.bitspilani.eon.eventOrganiser.ui

import ProfileBasicDetailFragment
import `in`.bitspilani.eon.BitsEonActivity
import `in`.bitspilani.eon.BitsEonApp
import `in`.bitspilani.eon.R
import `in`.bitspilani.eon.login.ui.ActionbarHost
import `in`.bitspilani.eon.login.ui.ChangePasswordFragment
import `in`.bitspilani.eon.utils.Constants
import `in`.bitspilani.eon.utils.ModelPreferencesManager
import `in`.bitspilani.eon.utils.UserType
import `in`.bitspilani.eon.utils.clickWithDebounce
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_user_profile.*
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber


/**
 * A simple [Fragment] subclass.
 */
class UserProfileFragment : Fragment(),CallbackListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        (activity as AppCompatActivity?)!!.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_profile, container, false)
    }

    /**
     * toggle visibility of different navigation
     */
    private var actionbarHost: ActionbarHost? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ActionbarHost) {
            actionbarHost = context
        }
    }

    override fun onResume() {
        super.onResume()
        actionbarHost?.showToolbar(showToolbar = true,title = "User Profile",showBottomNav = false)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        setHasOptionsMenu(false)
        (activity as AppCompatActivity?)!!.supportActionBar!!.setDisplayHomeAsUpEnabled(false)
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val activity = activity as? BitsEonActivity
        return when (item.itemId) {
            android.R.id.home -> {
                activity?.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onItemClick()
        if(ModelPreferencesManager.getInt(Constants.USER_ROLE)==UserType.ORGANISER.ordinal)
            profile_wish_list.visibility=View.GONE
    }


     private fun onItemClick() {
         profile_basic_details.clickWithDebounce {

             val dialogFragment = ProfileBasicDetailFragment(this)
             dialogFragment.show(childFragmentManager, "profileBasicDetail")

         }
         profile_wish_list.clickWithDebounce {}
         profile_change_password.clickWithDebounce {
             val dialogFragment = ChangePasswordFragment(this)
             dialogFragment.show(childFragmentManager, "changePassword")
         }
         profile_logout.clickWithDebounce {

             ModelPreferencesManager.clearCache()
             Timber.d("Cached cleared")
             //heavy task delay
             lifecycleScope.launch {
                 delay(200)
                 findNavController().navigate(R.id.signInFragment,
                     null,
                     NavOptions.Builder()
                         .setPopUpTo(R.id.app_nav,
                             true)
                         .build())
             }

         }

     }
    /**
     * get data from callback
     */
    override fun onDataReceived(data: String) {

    }


}


/*data class NavOptionItem(val key: String, val title: String, val icon: Int)*/
