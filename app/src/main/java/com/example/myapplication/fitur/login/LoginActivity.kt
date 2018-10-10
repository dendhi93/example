package com.example.myapplication.fitur.login

import android.os.Bundle
import com.example.myapplication.R
import com.example.myapplication.fitur.login.contract.LoginInterface
import com.example.myapplication.manager.ActivityManager

class LoginActivity : ActivityManager(), LoginInterface {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }


    override fun onShowDialog(messages: String, keyAction: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onShowMessage(messages: String, type: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}
