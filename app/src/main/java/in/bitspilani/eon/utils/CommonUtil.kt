package `in`.bitspilani.eon.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class CommonUtil {

    companion object {

        fun openLinkBrowser(activity: Activity, url: String) {

            val browserIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(url)
            )
            activity!!.startActivity(browserIntent)

        }


        fun formatDate(datestring: String, time: String): String {


            var dateFormat: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd");

            var date: Date = dateFormat.parse(datestring);

            val formatter: DateFormat = SimpleDateFormat("EEEEEEEEE, dd MMM yyyy ")
            val today = formatter.format(date)+" "+time


            return today
        }
    }
}