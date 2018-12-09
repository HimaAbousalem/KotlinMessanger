package com.example.abousalem.messengerapp.view.login

import com.example.abousalem.messengerapp.base.MvpView

interface LoginMvpView: MvpView{
    fun openLatestMessagesActivity()
    fun onLoginSuccess()
    fun onLoginFail(message: String)

}