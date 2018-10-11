package com.example.myapplication.fitur.dashboard

import android.databinding.DataBindingUtil
import android.os.Bundle
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityDashboardBinding
import com.example.myapplication.fitur.dashboard.contract.DashboardInterface
import com.example.myapplication.fitur.dashboard.viewmodel.DashboardViewModel
import com.example.myapplication.manager.ActivityManager
import android.support.v4.app.Fragment
import com.example.myapplication.fitur.dashboard.fragment.HomeFragment
import com.example.myapplication.fitur.dashboard.fragment.ProfileFragment

class DashboardActivity : ActivityManager(), DashboardInterface {

    private lateinit var binding: ActivityDashboardBinding
    private lateinit var viewModel: DashboardViewModel
    var selectedFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_dashboard)
        viewModel = DashboardViewModel(this, this)
        binding.viewModel = viewModel

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_dashLayout, HomeFragment.newInstance("",""))
        transaction.commit()

        binding.navigationDash.setOnNavigationItemSelectedListener {
            item ->
            when (item.itemId) {
                R.id.home_menu -> {
                    selectedFragment = HomeFragment.newInstance("","")
                }
                R.id.profile_menu -> {
                    selectedFragment = ProfileFragment.newInstance("","")
                }
            }
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.frame_dashLayout, selectedFragment)
            transaction.commit()
            true
        }
    }

    override fun onStart() {
        super.onStart()
        binding.viewModel.welcome()
    }

    override fun onShowMessage(messages: String) {
        snackbarMessage(messages)
    }



}
