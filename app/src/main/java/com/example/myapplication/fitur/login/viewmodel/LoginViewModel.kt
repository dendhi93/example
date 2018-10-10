package com.example.myapplication.fitur.login.viewmodel

import android.content.Context
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.os.Handler
import com.example.myapplication.R
import com.example.myapplication.application.BaseApplication
import com.example.myapplication.fitur.login.LoginActivity
import com.example.myapplication.fitur.login.contract.LoginInterface
import com.example.myapplication.connection.*

class LoginViewModel(val context: Context, val callback: LoginInterface){

    private val TAG = "LoginViewModel"
    private val isLogin = ObservableBoolean()
    val userName = ObservableField<String>()
    val password = ObservableField<String>()
    val isProgress = ObservableBoolean(false)


    fun processLogin(){
        when{
            userName.get().isNullOrEmpty() || password.get().isNullOrEmpty() -> callback.onShowMessage(context.getString(R.string.validate_data))
            else ->{
                when {
                    Connection.isNetworkAvailable(context) -> {
                        val handler = Handler()
                        handler.postDelayed({
                            isProgress.set(true)

                            BaseApplication.preferences!!.isLogin = true
                            BaseApplication.preferences!!.userName = userName.get().toString()
                            BaseApplication.preferences!!.pass = password.get().toString()
                            callback.onShowMessage("Login Success")
                            callback.onIntent()

                            isProgress.set(false)
                        },15000)
                    }
                    else -> callback.onShowDialog(context.getString(R.string.no_connection_2), LoginActivity.DIALOG_DIALOG_DISMISS)
                }
            }
        }

    }

}
