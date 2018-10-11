package com.example.myapplication.fitur.dashboard.viewmodel

import android.content.Context
import com.example.myapplication.application.BaseApplication
import com.example.myapplication.fitur.dashboard.contract.DashboardInterface

class DashboardViewModel(val context: Context, val callback: DashboardInterface){
    private val TAG = "DashboardViewModel"


    fun welcome(){
        callback.onShowMessage(BaseApplication.preferences!!.userName)
    }

}
