package com.example.abousalem.messengerapp.view.register

import android.net.Uri
import com.example.abousalem.messengerapp.base.MvpPresenter
import com.example.abousalem.messengerapp.model.User

interface RegisterMvpPresenter<V: RegisterMvpView>: MvpPresenter<V>{

    fun registerToFirebase(email: String, password: String)
    fun saveUserToFirebase(user: User)
    fun saveUserImageToFirebase(imgUri: Uri)
    fun openLoginActivity()

}