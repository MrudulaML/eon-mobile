package `in`.bitspilani.eon.utils

import `in`.bitspilani.eon.R
import android.app.Activity
import android.app.AlertDialog
import android.view.LayoutInflater
import kotlinx.android.synthetic.main.layout_dialog_payment_success.view.*

class SuccessDialog {


    companion object {


        @JvmStatic
        fun openDialog(activity: Activity, onOkayClick: () -> Unit) {

            val mDialogView =
                LayoutInflater.from(activity).inflate(R.layout.layout_dialog_payment_success, null)
            //AlertDialogBuilder
            val mBuilder = AlertDialog.Builder(activity)
                .setView(mDialogView)
            //show dialog
            val mAlertDialog = mBuilder.show()

            mDialogView.btn_okay.clickWithDebounce {

                onOkayClick.invoke()
                mAlertDialog.dismiss()
            }

        }

    }
}