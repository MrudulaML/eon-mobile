package `in`.bitspilani.eon.utils


import `in`.bitspilani.eon.event_organiser.models.FilterResponse
import android.content.Context
import android.os.SystemClock
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("app:`in`.bitspilani.eon.utils.formatDate")
fun TextView.formatDate( dateString: String, time : String) {

    text=CommonUtil.formatDate(dateString,time).toString()
}

@BindingAdapter("app:`in`.bitspilani.eon.utils.goneUnless")
fun View.goneUnless(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.GONE
}

@BindingAdapter("app:`in`.bitspilani.eon.utils.invisibleUnless")
fun View.invisibleUnless(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.INVISIBLE
}
@BindingAdapter("date")
fun formatDate(view:TextView,datestring: String) {

    var dateFormat: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd");
    var date: Date = dateFormat.parse(datestring);
    val formatter: DateFormat = SimpleDateFormat("EEEEEEEEE, dd MMM yyyy ")
    val today = formatter.format(date)
    view.text= today
}

var EditText.value
    get() = this.text.toString()
    set(value) {
        this.setText(value)
    }


@BindingAdapter("app:`in`.bitspilani.eon.utils.visibleUnless")
fun View.visibleUnless(hide: Boolean) {
    visibility = if (hide) View.GONE else View.VISIBLE
}

@BindingAdapter("app:`in`.bitspilani.eon.utils.goneIf")
fun View.goneIf(gone: Boolean) {
    visibility = if (gone) View.GONE else View.VISIBLE
}

@BindingAdapter("app:`in`.bitspilani.eon.utils.invisibleIf")
fun View.invisibleIf(invisible: Boolean) {
    visibility = if (invisible) View.INVISIBLE else View.VISIBLE
}

@BindingAdapter("app:`in`.bitspilani.eon.utils.visibleIf")
fun View.visibleIf(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.GONE
}


@BindingAdapter("app:`in`.bitspilani.eon.utils.invisibleWhen")
fun View.invisibleWhen(invisible: Boolean) {
    visibility = if (invisible) View.INVISIBLE else View.VISIBLE
}

@BindingAdapter("app:boundSrc")
fun ImageView.src(resource: Int) {
    setImageResource(resource)
}


@BindingAdapter("`in`.bitspilani.eon.utils.isSelected")
fun AppCompatImageView.isSelected(isSelected: Boolean) {
    setSelected(isSelected)
}

@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, imageUrl: String) {

    Picasso.get().load(imageUrl).into(view)
}

//TODO optimise this hacky thing
@BindingAdapter("eventType")
fun getType(view: TextView,id: Int) {
    val eventTypeCached = ModelPreferencesManager.get<FilterResponse>(Constants.EVENT_TYPES)
    val eventType = eventTypeCached?.data?.filter {

        it.id ==id
    }
    view.text=eventType?.get(0)?.type
}


@BindingAdapter("visibility")
fun setVisibility(view: View, value: Boolean) {
    view.visibility = if (value) View.VISIBLE else View.GONE
}

fun View.clickWithDebounce(debounceTime: Long = 1000L, action: () -> Unit) {
    this.setOnClickListener(object : View.OnClickListener {
        private var lastClickTime = 0.toLong()

        override fun onClick(v: View?) {
            if (SystemClock.elapsedRealtime() - lastClickTime < debounceTime) return
            else action()
            lastClickTime = SystemClock.elapsedRealtime()
        }

    })
}

/**
 * Extension method to show a keyboard for View.
 */
fun View.showKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    this.requestFocus()
    imm.showSoftInput(this, 0)
}
/**
 * Try to hide the keyboard and returns whether it worked
 * https://stackoverflow.com/questions/1109022/close-hide-the-android-soft-keyboard
 */
fun View.hideKeyboard(): Boolean {
    try {
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    } catch (ignored: RuntimeException) { }
    return false
}

/**
 * Shows the Snackbar inside an Activity or Fragment
 *
 * @param messageRes Text to be shown inside the Snackbar
 * @param length Duration of the Snackbar
 * @param f Action of the Snackbar
 */
fun View.showSnackbar(messageRes: String, length: Int = Snackbar.LENGTH_LONG) {
    val snackBar = Snackbar.make(this, messageRes, length)
    snackBar.show()
}






