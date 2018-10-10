package com.example.myapplication.manager

import android.content.Context
import android.support.v4.app.FragmentActivity
import com.example.myapplication.utilsinterface.DialogInterface

/**
 * Created by 15997068 on 24/05/2018.
 */
interface FunctionManager {
    fun snackbarMessage(string: String)
    fun showProgressBar(context: Context)
    fun hideProgressBar()
    fun dialogConfirmation(mActivity: FragmentActivity, dialogInterface: DialogInterface, string: String)
    fun dialogConfirmationDismiss(mActivity: FragmentActivity,string: String)
    fun dialogConfirmationDismissAction(mActivity: FragmentActivity, dialogInterface: DialogInterface, string: String)
    fun doubleTabExit()
    fun setUpTopBar()
}