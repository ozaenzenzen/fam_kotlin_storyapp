package com.example.famstoryappkotlin.utils
//
//import android.content.DialogInterface
//import android.widget.TextView
//import androidx.appcompat.app.AlertDialog
//import androidx.appcompat.widget.AlertDialogLayout
//import com.example.famstoryappkotlin.R
//
//fun AlertDialog.setBackgroundAndTextColors(backgroundColor: Int, titleTextColor: Int, messageTextColor: Int) {
//    getAlertDialogRootView(this)?.apply {
//        setBackgroundColor(backgroundColor)
//        findViewById<TextView?>(com.google.android.material.R.id.alertTitle)?.setTextColor(titleTextColor)
//        findViewById<TextView?>(android.R.id.message)?.setTextColor(messageTextColor)
//    }
//}
//
//private fun getAlertDialogRootView(alertDialog: AlertDialog): AlertDialogLayout? {
////    return alertDialog.mAlert.mScrollView.parent.parent as? AlertDialogLayout
//    return alertDialog.apply {  }
//}
//
//
//fun AlertDialog.setPositiveButtonTextColor(textColor: Int) {
//    getButton(DialogInterface.BUTTON_POSITIVE)?.setTextColor(textColor)
//}
//
//fun AlertDialog.setNegativeButtonTextColor(textColor: Int) {
//    getButton(DialogInterface.BUTTON_NEGATIVE)?.setTextColor(textColor)
//}