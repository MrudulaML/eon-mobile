package `in`.bitspilani.eon.event_organiser.ui

import `in`.bitspilani.eon.R
import `in`.bitspilani.eon.utils.clickWithDebounce
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlinx.android.synthetic.main.fragment_filter_event.*

class FilterEventFragment(val filterCallbackListener: FilterCallbackListener) : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_filter_event, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       // addChipToGroup("Music", chip_group)

        btn_filter.clickWithDebounce {

            filterCallbackListener.onApplyFilter(eventType = 1,fromFilter = true)
        }
    }

    private fun addChipToGroup(txt: String, chipGroup: ChipGroup) {
        val chip = Chip(context)
        chip.text = txt
//        chip.chipIcon = ContextCompat.getDrawable(requireContext(), baseline_person_black_18)
        chip.isCloseIconEnabled = true
        chip.setChipIconTintResource(R.color.green)

        // necessary to get single selection working
        chip.isClickable = false
        chip.isCheckable = false
        chipGroup.addView(chip as View)
        chip.setOnCloseIconClickListener { chipGroup.removeView(chip as View) }
        printChipsValue(chipGroup)
    }

    private fun printChipsValue(chipGroup: ChipGroup) {
        for (i in 0 until chipGroup.childCount) {
            val chipObj = chipGroup.getChildAt(i) as Chip
            //Log.d("Chips text :: " , chipObj.text.toString())

        }
    }



}