package com.example.abousalem.messengerapp.view.login

import com.example.abousalem.messengerapp.base.BasePresenter
import com.example.abousalem.messengerapp.model.DataManager

class LoginPresenter<V:LoginMvpView>: BasePresenter<V>, LoginMvpPresenter<V>{


    constructor(dataManager: DataManager) : super(dataManager)

    override fun loginToFirebase(email: String, password: String) {
        getDataManager().loginUser(email,password).addOnCompleteListener{
            if(!it.isSuccessful){
                getMvpView().onLoginFail(it.exception.toString())
                return@addOnCompleteListener
            }
            getMvpView().onLoginSuccess()

        }
    }
}