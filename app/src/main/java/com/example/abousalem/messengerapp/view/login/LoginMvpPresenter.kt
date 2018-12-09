package com.example.abousalem.messengerapp.view.login

import com.example.abousalem.messengerapp.base.MvpPresenter
import com.example.abousalem.messengerapp.base.MvpView

interface LoginMvpPresenter<in V:MvpView>:MvpPresenter<V> {
    fun loginToFirebase(email:String, password: String)
}