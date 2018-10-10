package com.example.myapplication.fitur.login

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import com.example.myapplication.R
import com.example.myapplication.application.BaseApplication
import com.example.myapplication.databinding.ActivityLoginBinding
import com.example.myapplication.fitur.dashboard.DashboardActivity
import com.example.myapplication.fitur.login.contract.LoginInterface
import com.example.myapplication.fitur.login.viewmodel.LoginViewModel
import com.example.myapplication.manager.ActivityManager
import com.example.myapplication.utilsinterface.DialogInterface

class LoginActivity : ActivityManager(), LoginInterface, DialogInterface {

    private lateinit var binding: ActivityLoginBinding
    private var KEY_DIALOG_ACTIVE = 0
    private lateinit var viewModel: LoginViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        viewModel = LoginViewModel(this, this)
        binding.viewModel = viewModel
    }


    override fun onShowDialog(messages: String, keyAction: Int) {
        when(keyAction){
            DIALOG_DIALOG_DISMISS ->{
                KEY_DIALOG_ACTIVE = DIALOG_DIALOG_DISMISS
                dialogConfirmationDismiss(this, messages)
            }
        }
    }

    override fun onShowMessage(messages: String) {
        snackbarMessage(messages)
    }

    override fun onNegativeClick(o: Any) {}

    override fun onPositiveClick(o: Any) {}

    override fun onBackPressed() {
        super.onBackPressed()
        doubleTabExit()
    }

    companion object {
        const val DIALOG_DIALOG_DISMISS = 1
    }

    override fun onIntent() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onResume() {
        super.onResume()
        if (BaseApplication.preferences!!.isLogin) {
            startActivity(Intent(this, DashboardActivity::class.java))
            finish()
        }
    }

}
