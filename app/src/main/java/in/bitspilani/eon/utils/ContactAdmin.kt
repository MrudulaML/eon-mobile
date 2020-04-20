package `in`.bitspilani.eon.utils

import `in`.bitspilani.eon.R
import android.app.Activity
import android.app.AlertDialog
import android.view.LayoutInflater
import android.widget.Toast
import kotlinx.android.synthetic.main.layout_dialog_contact_admin.view.*
import kotlinx.android.synthetic.main.layout_email_share_to_friend.view.*

class ContactAdmin {

    companion object {

        lateinit var activity: Activity

        lateinit var mAlertDialog: AlertDialog
        @JvmStatic
        fun openDialog(
            activity: Activity,
            onSendClick: (emailId: String, message: String) -> Unit
        ) {

            this@Companion.activity = activity

            val mDialogView =
                LayoutInflater.from(activity).inflate(R.layout.layout_dialog_contact_admin, null)
            //AlertDialogBuilder
            val mBuilder = AlertDialog.Builder(activity)
                .setView(mDialogView)
            //show dialog
            val mAlertDialog = mBuilder.show()
            this@Companion.mAlertDialog = mAlertDialog

            mDialogView.btn_okay.clickWithDebounce {


            }

        }

        @JvmStatic
        fun showUserMsg(msg: String) {
            Toast.makeText(activity, msg, Toast.LENGTH_LONG).show()

        }

        @JvmStatic
        fun dismissDialog() {

            mAlertDialog.dismiss()

        }
    }
}