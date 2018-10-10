package com.example.myapplication.utils

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import com.sdsmdg.tastytoast.TastyToast



fun AppCompatActivity.replaceFragmentInActivity(fragment: Fragment, frameId: Int){
    supportFragmentManager.transact {
        replace(frameId,fragment)
    }
}

fun Context.toast(message: CharSequence, type: Int) {
    val toastMessage = TastyToast.makeText(this, "   $message   ", TastyToast.LENGTH_LONG,type)
//    val viewToast = toastMessage.view
//    viewToast.setBackgroundColor(Color.BLUE)
    toastMessage.setGravity(Gravity.CENTER, 0,0)
    toastMessage.show()
}

private inline fun FragmentManager.transact(action: FragmentTransaction.() -> Unit){
    beginTransaction().apply{
        action()
    }.commit()
}