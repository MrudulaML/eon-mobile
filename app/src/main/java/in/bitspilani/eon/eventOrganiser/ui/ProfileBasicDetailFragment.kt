import `in`.bitspilani.eon.BitsEonActivity
import `in`.bitspilani.eon.BitsEonApp
import `in`.bitspilani.eon.R
import `in`.bitspilani.eon.eventOrganiser.ui.CallbackListener
import `in`.bitspilani.eon.utils.clickWithDebounce
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.CheckBox
import android.widget.GridLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_profile_basic_detail.*

/**
 * A simple [Fragment] subclass.
 */
class ProfileBasicDetailFragment(private val callbackListener: CallbackListener) : DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isCancelable = false
        return inflater.inflate(R.layout.fragment_profile_basic_detail, container, false)
    }
    override fun getTheme(): Int {
        return R.style.DialogTheme
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //send data
        //callbackListener.onDataReceived(editText.text.toString())
        btn_close.clickWithDebounce { dismiss() }
        btn_basic_cancel.clickWithDebounce {   dismiss() }
        btn_basic_confirm.clickWithDebounce {   dismiss() }

        populateCheckBox()




    }

    fun populateCheckBox(){

        val checkBox1 = CheckBox(context)
        checkBox1.text = "First CheckBox"
        checkBox1.isChecked = true
        val checkBox2 = CheckBox(context)
        checkBox2.text = "Second Checkbox"
        checkbox_layout.addView(checkBox1)
        checkbox_layout.addView(checkBox2)


    }
}