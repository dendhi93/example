package com.example.myapplication.utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentActivity
import android.widget.TextView
import com.example.myapplication.R

/**
 * Created by 15997063 on 28/09/2017.
 */
class DialogConfirmationDismiss : DialogFragment() {
    private var message: String? = null
    private var title: String? = null
    private var titleTextView : TextView? = null
    private var messageTextView: TextView? = null
    private var mActivity: FragmentActivity? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        when (context) {
            is Activity -> mActivity = context as FragmentActivity
            else -> if(activity != null) mActivity = activity
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)
        message = arguments!!.getString(Constants.KEY_MESSAGE)
        title = arguments!!.getString(Constants.KEY_TITLE)
        val inflater = mActivity!!.layoutInflater
        val view = inflater.inflate(R.layout.custom_dialog_confirmation, null)
        titleTextView = view.findViewById(R.id.dialog_confirmation_title)
        messageTextView = view.findViewById(R.id.dialog_confirmation_message)
        titleTextView!!.text = title
        messageTextView!!.text = message
        val builder = activity?.let {
            android.support.v7.app.AlertDialog.Builder(it)
                .setIcon(R.mipmap.ic_launcher)
                .setTitle(title)
                .setCancelable(false)
                .setMessage(message)
                .setPositiveButton("OK") { _,_ ->
                    dismiss()
                }
        }
        isCancelable = false
        return builder!!.create()
    }
}