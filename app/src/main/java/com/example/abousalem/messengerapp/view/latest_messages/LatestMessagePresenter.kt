package com.example.abousalem.messengerapp.view.latest_messages

import com.example.abousalem.messengerapp.base.BasePresenter
import com.example.abousalem.messengerapp.model.ChatMessage
import com.example.abousalem.messengerapp.model.DataManager
import com.example.abousalem.messengerapp.model.User
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class LatestMessagePresenter<V: LatestMessageMvpView>: BasePresenter<V>, LatestMessageMvpPresenter<V>{

    constructor(dataManager: DataManager) : super(dataManager)

    override fun IsUserLoggedIn() {
        if(!getDataManager().IsuserLoggedIn()){
            getMvpView().openRegisterActivity()
        }else{
            fetchCurrentUser()
        }
    }

    override fun fetchCurrentUser() {
        getDataManager().getCurrentUser().addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                val currentUser = p0.getValue(User::class.java)?: return
                getMvpView().getCurrentUser(currentUser)
            }

            override fun onCancelled(p0: DatabaseError) {}

        })
    }

    override fun listenForLatestMessages() {
        getDataManager().getLatestMessages().addValueEventListener(object :ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                    var latestMessagesMap = HashMap<String,ChatMessage>()
                    p0.children.forEach {
                        val chatMessage = it.getValue(ChatMessage::class.java)?: return
                        latestMessagesMap[it.key!!] = chatMessage
                    }
                    getMvpView().refreshRecyclerView(latestMessagesMap)
            }

            override fun onCancelled(p0: DatabaseError) {
            }


        })
    }
}