package com.example.abousalem.messengerapp.view.register

import android.net.Uri
import com.example.abousalem.messengerapp.base.BasePresenter
import com.example.abousalem.messengerapp.model.DataManager
import com.example.abousalem.messengerapp.model.User

class RegisterPresenter<V: RegisterMvpView>: BasePresenter<V>, RegisterMvpPresenter<V>{


    constructor(dataManager: DataManager) : super(dataManager)

    override fun registerToFirebase(email: String, password: String) {
        getDataManager().registerUser(email,password).addOnCompleteListener{
            if(!it.isSuccessful){
                getMvpView().onRegisterFail(it.exception!!.toString())
                return@addOnCompleteListener
            }
            getMvpView().onRegisterSuccess(getDataManager().getUid())

        }.addOnFailureListener{
            getMvpView().onRegisterFail(it.message!!)
        }
    }

    override fun saveUserToFirebase(user: User) {
        getDataManager().getSaveUserDataRef(user).addOnSuccessListener {
            getMvpView().openLatestMessagesActivity()
        }
    }

    override fun saveUserImageToFirebase(imgUri: Uri) {
        val ref = getDataManager().getUserImageRef()
        ref.putFile(imgUri).addOnSuccessListener {
            ref.downloadUrl.addOnSuccessListener {
                val profileImageUrl =it.toString()
                getMvpView().onSaveImageSuccess(getDataManager().getUid(),profileImageUrl)
            }

        }
    }
    override fun openLoginActivity() {
        getMvpView().openLoginActivity()
    }

}