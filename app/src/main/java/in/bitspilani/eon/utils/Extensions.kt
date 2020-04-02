package `in`.bitspilani.eon.utils

import android.os.SystemClock
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.Group
import androidx.databinding.BindingAdapter


@BindingAdapter("app:`in`.bitspilani.eon.utils.goneUnless")
fun View.goneUnless(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.GONE
}

@BindingAdapter("app:`in`.bitspilani.eon.utils.invisibleUnless")
fun View.invisibleUnless(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.INVISIBLE
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




