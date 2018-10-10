package com.example.myapplication.fitur.login.contract

interface LoginInterface{

    fun onShowMessage(messages: String)
    fun onShowDialog(messages: String, keyAction: Int)
    fun onIntent()
}
