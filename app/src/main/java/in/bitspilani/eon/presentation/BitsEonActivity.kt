package `in`.bitspilani.eon.presentation

import `in`.bitspilani.eon.R
import `in`.bitspilani.eon.databinding.ActivityBitsEonBinding
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation

class BitsEonActivity : AppCompatActivity() {
    lateinit var navController: NavController
    lateinit var binding: ActivityBitsEonBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_bits_eon)
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
    }

}
