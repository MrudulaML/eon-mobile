package `in`.bitspilani.eon.utils

import `in`.bitspilani.eon.R
import android.app.Activity
import android.app.AlertDialog
import android.view.LayoutInflater
import android.widget.Toast
import kotlinx.android.synthetic.main.layout_dialog_payment_success.view.*
import kotlinx.android.synthetic.main.layout_tnc.view.*

class TermsAndConditionDialog {

    companion object {
        lateinit var activity: Activity



        @JvmStatic
        fun openDialog(activity: Activity, onAcceptClick: () -> Unit) {


            this@Companion.activity = activity

            val mDialogView =
                LayoutInflater.from(activity).inflate(R.layout.layout_tnc, null)
            //AlertDialogBuilder
            val mBuilder = AlertDialog.Builder(activity)
                .setView(mDialogView)
            //show dialog
            val mAlertDialog = mBuilder.show()

            mDialogView.iv_cross.clickWithDebounce {
                mAlertDialog.dismiss()
            }

            mDialogView.btn_accept.clickWithDebounce {

                if (mDialogView.cb_tnc.isChecked) {
                    onAcceptClick.invoke()
                    mAlertDialog.dismiss()
                } else {

                    showUserMsg("Please select the checkbox")

                }


            }

        }

        @JvmStatic
        fun showUserMsg(msg: String) {
            Toast.makeText(SendEmailDialog.activity, msg, Toast.LENGTH_LONG).show()

        }
    }
}