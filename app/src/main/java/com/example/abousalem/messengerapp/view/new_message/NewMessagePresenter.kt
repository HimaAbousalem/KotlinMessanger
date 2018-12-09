package com.example.abousalem.messengerapp.view.new_message

import com.example.abousalem.messengerapp.base.BasePresenter
import com.example.abousalem.messengerapp.model.DataManager
import com.example.abousalem.messengerapp.model.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class NewMessagePresenter<V:NewMessageMvpView>: BasePresenter<V>, NewMessageMvpPresenter<V>{
    constructor(dataManager: DataManager) : super(dataManager)

    override fun fetchUsers() {
        getDataManager().getUsersRef().addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                var users = ArrayList<User>()
                p0.children.forEach {
                    val user = it.getValue(User::class.java)
                    if(user != null && user.uid != getDataManager().getUid()){
                        users.add(user)
                    }
                }
                getMvpView().updateNewMessageAdapter(users)
            }

            override fun onCancelled(p0: DatabaseError) {}

        })
    }

}