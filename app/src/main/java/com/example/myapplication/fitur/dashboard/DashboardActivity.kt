package com.example.myapplication.fitur.dashboard

import android.databinding.DataBindingUtil
import android.os.Bundle
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityDashboardBinding
import com.example.myapplication.fitur.dashboard.contract.DashboardInterface
import com.example.myapplication.fitur.dashboard.viewmodel.DashboardViewModel
import com.example.myapplication.manager.ActivityManager

class DashboardActivity : ActivityManager(), DashboardInterface {

    private lateinit var binding: ActivityDashboardBinding
    private lateinit var viewModel: DashboardViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_dashboard)
        viewModel = DashboardViewModel(this, this)
        binding.viewModel = viewModel
    }


    override fun onShowMessage(messages: String) {
        snackbarMessage(messages)
    }



}
