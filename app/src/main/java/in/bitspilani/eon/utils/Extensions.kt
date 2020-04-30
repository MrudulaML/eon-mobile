package `in`.bitspilani.eon.utils


import `in`.bitspilani.eon.R
import `in`.bitspilani.eon.event_organiser.models.FilterResponse
import android.content.Context
import android.content.res.Resources
import android.net.Uri
import android.os.SystemClock
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateUtils
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


@BindingAdapter("app:`in`.bitspilani.eon.utils.formatDate")
fun TextView.formatDate(dateString: String, time: String) {

    text = CommonUtil.formatDate(dateString, time).toString()
}

@BindingAdapter("app:`in`.bitspilani.eon.utils.goneUnless")
fun View.goneUnless(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.GONE
}

@BindingAdapter("attachImage")
fun ImageView.attachImage(uri: Uri?) {

    if (uri != null) {

        setImageURI(uri)
        visibility = View.VISIBLE
    } else {

        visibility = View.GONE
    }
}

@BindingAdapter("goneUnless")
fun LinearLayout.goneUnless(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.GONE
}


@BindingAdapter("goneUnless")
fun TextView.goneUnless(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.GONE
}

@BindingAdapter("app:`in`.bitspilani.eon.utils.invisibleUnless")
fun View.invisibleUnless(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.INVISIBLE
}

@BindingAdapter("timeAgo")
fun getPrettyTime(view: TextView, date: String) {

    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    sdf.timeZone = TimeZone.getTimeZone("GMT")
    try {
        val time = sdf.parse(date).time
        val now = System.currentTimeMillis()

        view.text = DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
}

@BindingAdapter("date")
fun formatDate(view: TextView, datestring: String) {

    var dateFormat: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd");
    var date: Date = dateFormat.parse(datestring);
    val formatter: DateFormat = SimpleDateFormat("EEEEEEEEE, dd MMM yyyy ")
    val today = formatter.format(date)
    view.text = today
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
    visibility = if (visible) View.VISIBLE else View.INVISIBLE
}

@BindingAdapter("imageUrl")
fun invisibleWhen(imageView: ImageView, url: String) {

    Picasso.get().load(url).into(imageView)
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
fun loadImage(view: ImageView, imageUrl: String?) {

    if(imageUrl!=null && !imageUrl.isEmpty())
    Picasso.get().load(imageUrl).into(view)
      else{
        Picasso.get().load(R.drawable.dummy_image).into(view)

    }
}

fun EditText.onChange(cb: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            cb(s.toString())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    })
}

//TODO optimise this hacky thing
@BindingAdapter("eventType")
fun getType(view: TextView, id: Int) {
    val eventTypeCached = ModelPreferencesManager.get<FilterResponse>(Constants.EVENT_TYPES)
    val eventType = eventTypeCached?.data?.filter {

        it.id == id
    }
    view.text = eventType?.get(0)?.type
}

@BindingAdapter("eventStatus")
fun setEventStatus(view : TextView, status : String ){

    if(status.toLowerCase().equals("upcoming")){

        view.text="Upcoming"
        view.setBackgroundResource(R.drawable.stroke_orange_rectangle)
        view.setTextColor(view.resources.getColor(R.color.orange_status))

    }

    if(status.toLowerCase().equals("completed")){

        view.text="Completed"
        view.setBackgroundResource(R.drawable.stroke_green_rectangle)
        view.setTextColor(view.resources.getColor(R.color.green_status))

    }

    if(status.toLowerCase().equals("cancelled")){

        view.text="Cancelled"
        view.setBackgroundResource(R.drawable.stroke_red_rectangle)
        view.setTextColor(view.resources.getColor(R.color.red_status))

    }

}




@BindingAdapter("organizedEventStatus")
fun setOrganizedEventStatus(view : TextView, status : String ){

    if(status.toLowerCase().equals("upcoming")){

        view.text=status.capitalize()
        view.setTextColor(view.resources.getColor(R.color.orange_status))

    }

    if(status.toLowerCase().equals("completed")){

        view.text=status.capitalize()
        view.setTextColor(view.resources.getColor(R.color.green_status))

    }

    if(status.toLowerCase().equals("cancelled")){

        view.text=status.capitalize()
        view.setTextColor(view.resources.getColor(R.color.red_status))

    }

}

@BindingAdapter("statusVisibility")
fun setDeleteVisibility(view: View, value: String) {
    view.visibility = if (value.equals("completed") || value.equals("cancelled") ) View.GONE else View.VISIBLE
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
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    } catch (ignored: RuntimeException) {
    }
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

val Int.dp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()
val Int.px: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()


@BindingAdapter("time")
fun convertTime(view: TextView, dateInString: String)
{

    var inputFormat=SimpleDateFormat("hh:mm:ss", Locale.ENGLISH)
    var outputFormat=SimpleDateFormat("KK:mm a", Locale.ENGLISH)

    view.text=outputFormat.format(inputFormat.parse(dateInString)!!)

}


@BindingAdapter("dateAndTime")
fun convertDateAndTime(view: TextView, dateInString: String)
{

    var inputFormat=SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH)
    var outputFormat=SimpleDateFormat("KK:mm a EEE, dd MMM yyyy ", Locale.ENGLISH)

    view.text=outputFormat.format(inputFormat.parse(dateInString)!!)

}




