package com.example.abousalem.messengerapp.view.register

import com.example.abousalem.messengerapp.base.MvpView

interface RegisterMvpView: MvpView {
    fun openLoginActivity()
    fun openLatestMessagesActivity()
    fun onRegisterSuccess(uid: String)
    fun onRegisterFail(message: String)
    fun onSaveImageSuccess(uid: String,profileImage: String)
}