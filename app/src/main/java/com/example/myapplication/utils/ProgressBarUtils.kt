package com.example.myapplication.utils

import android.app.Activity
import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout

/**
 * Created by Deddy on 24/05/2018.
 */

class ProgressBarUtils(mContext: Context) {

    private val mProgressBar: ProgressBar

    init {
        val layout = (mContext as Activity).findViewById<View>(android.R.id.content).rootView as ViewGroup
        mProgressBar = ProgressBar(mContext, null, android.R.attr.progressBarStyleLargeInverse)
        mProgressBar.isIndeterminate = true

        val params = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT)
        val rl = RelativeLayout(mContext)

        rl.gravity = Gravity.CENTER
        rl.addView(mProgressBar)
        layout.addView(rl, params)

        hide()
    }

    fun show() {
        mProgressBar.visibility = View.VISIBLE
    }

    fun hide() {
        mProgressBar.visibility = View.INVISIBLE
    }
}