package com.example.myapplication.fitur.preparation

import android.Manifest
import android.annotation.TargetApi
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import android.view.ViewGroup
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityPermissionBinding
import com.example.myapplication.fitur.login.LoginActivity
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.PermissionRequestErrorListener
import com.karumi.dexter.listener.multi.CompositeMultiplePermissionsListener
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.multi.SnackbarOnAnyDeniedMultiplePermissionsListener

class PermissionActivity : AppCompatActivity() {

    private lateinit var binding : ActivityPermissionBinding
    private var allPermissionsListener: MultiplePermissionsListener? = null
    private var errorListener: PermissionRequestErrorListener? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_permission)

        createPermissionListeners()
        checkPermission()
    }


    private fun createPermissionListeners() {
        val feedbackViewMultiplePermissionListener = NewMultiplePermission()
        allPermissionsListener = CompositeMultiplePermissionsListener(feedbackViewMultiplePermissionListener,
                SnackbarOnAnyDeniedMultiplePermissionsListener.Builder.with(binding.root.rootView as ViewGroup,
                        R.string.all_permissions_denied_feedback)
                        .withOpenSettingsButton(R.string.permission_rationale_settings_button_text)
                        .build())
        errorListener = PermissionRequestErrorListener { error -> Log.e("Dexter", "There was an error: " + error.toString()) }
    }

    private inner class NewMultiplePermission internal constructor() : MultiplePermissionsListener {

        override fun onPermissionsChecked(report: MultiplePermissionsReport) {
            for (response in report.grantedPermissionResponses) {
                if (report.areAllPermissionsGranted()) {
                    checkPermission()
                }
            }
        }

        @TargetApi(Build.VERSION_CODES.M)
        override fun onPermissionRationaleShouldBeShown(permissions: List<PermissionRequest>, token: PermissionToken) {
            showPermissionRationale(token)
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun showPermissionRationale(token: PermissionToken) {
        AlertDialog.Builder(this).setTitle(R.string.permission_rationale_title)
                .setMessage(R.string.permission_rationale_message)
                .setNegativeButton(android.R.string.cancel) { dialog, _->
                    dialog.dismiss()
                    token.cancelPermissionRequest()
                }
                .setPositiveButton(android.R.string.ok) { dialog, _ ->
                    dialog.dismiss()
                    token.continuePermissionRequest()
                }
                .setOnDismissListener { token.cancelPermissionRequest() }
                .show()
    }

    fun onAllPermissionsButtonClicked(view : View) {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.ACCESS_WIFI_STATE,
                        Manifest.permission.READ_PHONE_STATE)
                .withListener(allPermissionsListener)
                .withErrorListener(errorListener)
                .check()
    }

    fun checkPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            val intent = Intent(this@PermissionActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        } else if (
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE) == PackageManager.PERMISSION_GRANTED) {

            val intent = Intent(this@PermissionActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
