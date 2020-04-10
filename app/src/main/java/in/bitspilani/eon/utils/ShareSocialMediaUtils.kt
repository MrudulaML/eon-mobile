package `in`.bitspilani.eon.utils

import `in`.bitspilani.eon.R
import android.content.Intent
import android.content.ComponentName
import android.content.pm.PackageManager
import android.app.Activity
import android.net.Uri
import android.text.TextUtils
import android.widget.Toast
import android.util.Log
import java.io.UnsupportedEncodingException
import java.net.URLEncoder


class ShareSocialMediaUtils {

    // Share on Facebook. Using Facebook app if installed or web link otherwise.
    fun shareFacebook(activity: Activity, text: String, url: String) {
        var facebookAppFound = false
        var shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, url)
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(url))

        val pm = activity.packageManager
        val activityList = pm.queryIntentActivities(shareIntent, 0)
        for (app in activityList) {
            if (app.activityInfo.packageName.contains("com.facebook.katana")) {
                val activityInfo = app.activityInfo
                val name =
                    ComponentName(activityInfo.applicationInfo.packageName, activityInfo.name)
                shareIntent.addCategory(Intent.CATEGORY_LAUNCHER)
                shareIntent.component = name
                facebookAppFound = true
                break
            }
        }
        if (!facebookAppFound) {
            val sharerUrl = "https://www.facebook.com/sharer/sharer.php?u=$url"
            shareIntent = Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl))
        }
        activity.startActivity(shareIntent)
    }

    // Share on Twitter. Using Twitter app if installed or web link otherwise.
    fun shareTwitter(activity: Activity, text: String, url: String, via: String, hashtags: String) {
        val tweetUrl = StringBuilder("https://twitter.com/intent/tweet?text=")
        tweetUrl.append(if (TextUtils.isEmpty(text)) urlEncode(" ") else urlEncode(text))
        if (!TextUtils.isEmpty(url)) {
            tweetUrl.append("&url=")
            tweetUrl.append(urlEncode(url))
        }
        if (!TextUtils.isEmpty(via)) {
            tweetUrl.append("&via=")
            tweetUrl.append(urlEncode(via))
        }
        if (!TextUtils.isEmpty(hashtags)) {
            tweetUrl.append("&hastags=")
            tweetUrl.append(urlEncode(hashtags))
        }
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(tweetUrl.toString()))
        val matches = activity.packageManager.queryIntentActivities(intent, 0)
        for (info in matches) {
            if (info.activityInfo.packageName.toLowerCase().startsWith("com.twitter")) {
                intent.setPackage(info.activityInfo.packageName)
            }
        }
        activity.startActivity(intent)
    }

    // Share on Whatsapp (if installed)
    fun shareWhatsApp(activity: Activity, text: String, url: String) {
        val pm = activity.packageManager
        try {

            val waIntent = Intent(Intent.ACTION_SEND)
            waIntent.type = "text/plain"

            val info = pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA)
            //Check if package exists or not. If not then code
            //in catch block will be called
            waIntent.setPackage("com.whatsapp")

            waIntent.putExtra(Intent.EXTRA_TEXT, "$text $url")
            activity.startActivity(
                Intent
                    .createChooser(waIntent, activity.getString(R.string.share_intent_title))
            )

        } catch (e: PackageManager.NameNotFoundException) {
            Toast.makeText(
                activity, activity.getString(R.string.share_whatsapp_not_installed),
                Toast.LENGTH_SHORT
            ).show()
        }

    }
    // Convert to UTF-8 text to put it on url format
    fun urlEncode(s: String): String {
        try {
            return URLEncoder.encode(s, "UTF-8")
        } catch (e: UnsupportedEncodingException) {
            Log.wtf("wtf", "UTF-8 should always be supported", e)
            throw RuntimeException("URLEncoder.encode() failed for $s")
        }

    }
}