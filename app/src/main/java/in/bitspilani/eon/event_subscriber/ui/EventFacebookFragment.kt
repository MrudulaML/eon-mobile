package `in`.bitspilani.eon.event_subscriber.ui

import `in`.bitspilani.eon.R
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.facebook.CallbackManager
import com.facebook.share.model.ShareLinkContent
import com.facebook.share.model.SharePhoto
import com.facebook.share.model.SharePhotoContent
import com.facebook.share.widget.ShareButton
import com.facebook.share.widget.ShareDialog


class EventFacebookFragment : Fragment() {

    var shareDialog: ShareDialog? = null
    var callbackManager: CallbackManager? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_facebook, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var shareTextButton = view.findViewById<Button>(R.id.btn_share)

        callbackManager = CallbackManager.Factory.create()
        shareDialog = ShareDialog(this)

        shareTextButton.setOnClickListener(View.OnClickListener {
            if (ShareDialog.canShow(ShareLinkContent::class.java)){

                val shareLinkContent = ShareLinkContent.Builder()
                    .setContentTitle("Share text on facebook")
                    .setQuote("This is useful link")
                    .setContentUrl(Uri.parse("https://developers.facebook.com"))
                    .build()
                shareDialog?.show(shareLinkContent)
            }
        })
        setImageShare(view)
    }

    private fun setImageShare(view: View) {
        val image = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)
        val photo = SharePhoto.Builder()
            .setBitmap(image)
            .setCaption("Bits Pilani")
            .build()
        val content = SharePhotoContent.Builder()
            .addPhoto(photo)
            .build()

        val shareButton = view.findViewById<View>(R.id.fb_share_button) as ShareButton
        shareButton.shareContent = content
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager?.onActivityResult(requestCode, resultCode, data);
    }
}