package com.example.myapplication.fitur.login.contract

interface LoginInterface{

    fun onShowMessage(messages: String, type : Int)
    fun onShowDialog(messages: String, keyAction: Int)
}
