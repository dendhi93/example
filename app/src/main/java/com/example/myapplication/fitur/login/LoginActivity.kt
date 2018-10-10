package com.example.myapplication.fitur.login

import android.os.Bundle
import com.example.myapplication.R
import com.example.myapplication.fitur.login.contract.LoginInterface
import com.example.myapplication.fitur.login.viewmodel.LoginViewModel
import com.example.myapplication.manager.ActivityManager
import com.example.myapplication.utilsinterface.DialogInterface

class LoginActivity : ActivityManager(), LoginInterface, DialogInterface {

    private var KEY_DIALOG_ACTIVE = 0
    private lateinit var viewModel: LoginViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }


    override fun onShowDialog(messages: String, keyAction: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onShowMessage(messages: String) {
        snackbarMessage(messages)
    }

    override fun onNegativeClick(o: Any) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPositiveClick(o: Any) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}
