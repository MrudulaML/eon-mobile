package `in`.bitspilani.eon.utils


import android.content.Context
import android.os.SystemClock
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso

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






