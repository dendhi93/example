package com.example.myapplication.manager

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Gravity
import android.widget.TextView
import com.example.myapplication.R
import com.example.myapplication.utilsinterface.DialogInterface
import com.sdsmdg.tastytoast.TastyToast
import com.example.myapplication.utils.*


open class ActivityManager: AppCompatActivity(),FunctionManager{
    override fun setUpTopBar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private var prograssBar: ProgressBarUtils? = null

    companion object {
        const val TAG = "ActivityManager"
    }

    override fun dialogConfirmation(mActivity: FragmentActivity, dialogInterface: DialogInterface, string: String) {
        try {
            when{
                !mActivity.isFinishing ->{
                    val dialog = DialogConfirmation()
                    dialog.setOnClick(dialogInterface)
                    val bundle = Bundle()
                    bundle.putString(Constants.KEY_MESSAGE, string)
                    bundle.putString(Constants.KEY_TITLE, "Confirmation")
                    dialog.arguments = bundle
                    dialog.show(mActivity.supportFragmentManager, "DialogConfirmation")
                }
            }
        }catch (e : Exception){
            Log.d(TAG,"Can't show dialog",e)
        }

    }

    override fun dialogConfirmationDismiss(mActivity: FragmentActivity, string: String) {
        try {
            when{
                !mActivity.isFinishing ->{
                    val dialog = DialogConfirmationDismiss()
                    val bundle = Bundle()
                    bundle.putString(Constants.KEY_MESSAGE, string)
                    bundle.putString(Constants.KEY_TITLE, "Confirmation")
                    dialog.arguments = bundle
                    dialog.show(mActivity.supportFragmentManager, "DialogConfirmationDismiss")
                }
            }
        }catch (e: Exception){
            Log.d(TAG,"Can't show dialog",e)
        }
    }

    override fun dialogConfirmationDismissAction(mActivity: FragmentActivity, dialogInterface: DialogInterface, string: String) {
        try {
            when{
                !mActivity.isFinishing ->{
                    val dialog = DialogConfirmationYesAction()
                    dialog.setOnClick(dialogInterface)
                    val bundle = Bundle()
                    bundle.putString(Constants.KEY_MESSAGE, string)
                    bundle.putString(Constants.KEY_TITLE, "Confirmation")
                    dialog.arguments = bundle
                    dialog.show(mActivity.supportFragmentManager, "DialogConfirmationDismissAction")
                }
            }
        }catch (e : Exception){
            Log.d(TAG,"Can't show dialog",e)
        }

    }

    override fun snackbarMessage(string: String) {
        val tv : TextView
        val snackbar = Snackbar.make(window.decorView, string, Snackbar.LENGTH_LONG)
        tv = snackbar.view.findViewById(android.support.design.R.id.snackbar_text)
        tv.setTextColor(ContextCompat.getColor(applicationContext, R.color.colorWhite))
        tv.gravity = Gravity.CENTER_HORIZONTAL
        snackbar.show()
    }

    override fun showProgressBar(context: Context) {
        prograssBar = ProgressBarUtils(context)
        prograssBar!!.show()
    }

    override fun hideProgressBar() {
        prograssBar?.hide()
    }

    private var isDoubleTab = false
    override fun doubleTabExit() {
        if(isDoubleTab){
            super.onBackPressed()
            val startMain = Intent(Intent.ACTION_MAIN)
            startMain.addCategory(Intent.CATEGORY_HOME)
            startMain.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(startMain)
            return
        }
        this.isDoubleTab = true
        val toastMessage = TastyToast.makeText(this, "Tap sekali lagi untuk keluar", TastyToast.LENGTH_SHORT,Constants.T_WARNING)
        val viewToast = toastMessage.view
        viewToast.setBackgroundColor(Color.BLUE)
        toastMessage.setGravity(Gravity.CENTER, 0,0)
        toastMessage.show()
        Handler().postDelayed({ isDoubleTab = false }, 2000)
    }

}
