package `in`.bitspilani.eon.eventOrganiser.ui

import ProfileBasicDetailFragment
import `in`.bitspilani.eon.BitsEonActivity
import `in`.bitspilani.eon.R
import `in`.bitspilani.eon.utils.clickWithDebounce
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_user_profile.*


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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity?.title = "My Profile"
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
        onItemClickClick()
    }

     private fun onItemClickClick() {
         profile_basic_details.clickWithDebounce {

             val dialogFragment = ProfileBasicDetailFragment(this)
             dialogFragment.show(childFragmentManager, "profileBasicDetail")

         }
         profile_wish_list.clickWithDebounce {}
         profile_change_password.clickWithDebounce { findNavController().navigate(R.id.changePasswordFragment) }
         profile_logout.clickWithDebounce {}

     }
    /**
     * get data from callback
     */
    override fun onDataReceived(data: String) {

    }


}


/*data class NavOptionItem(val key: String, val title: String, val icon: Int)*/
