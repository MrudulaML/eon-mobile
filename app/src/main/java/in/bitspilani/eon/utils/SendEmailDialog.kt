package `in`.bitspilani.eon.utils

import `in`.bitspilani.eon.R
import android.app.Activity
import android.app.AlertDialog
import android.view.LayoutInflater
import android.widget.Toast
import kotlinx.android.synthetic.main.layout_email_share_to_friend.view.*

class SendEmailDialog {


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
                LayoutInflater.from(activity).inflate(R.layout.layout_email_share_to_friend, null)
            //AlertDialogBuilder
            val mBuilder = AlertDialog.Builder(activity)
                .setView(mDialogView)
            //show dialog
            val mAlertDialog = mBuilder.show()
            this@Companion.mAlertDialog=mAlertDialog

            mDialogView.btn_share.clickWithDebounce {

                if (mDialogView.et_email_id.text.isEmpty())
                    showUserMsg("Please enter an email id")
                if (!EmailValidator.isEmailValid(mDialogView.et_email_id.text.toString()))
                    showUserMsg("Please enter a valid email id")
                else if (mDialogView.et_message.text.isEmpty())
                    showUserMsg("Please enter some message")
                else {
                    onSendClick.invoke(mDialogView.et_email_id.text.toString(), mDialogView.et_message.text.toString())
                    mAlertDialog.dismiss()
                }

            }

        }

        @JvmStatic
        fun showUserMsg(msg: String) {
            Toast.makeText(activity, msg, Toast.LENGTH_LONG).show()

        }

        @JvmStatic
        fun dismissDialog(){

            mAlertDialog.dismiss()

        }
    }
}