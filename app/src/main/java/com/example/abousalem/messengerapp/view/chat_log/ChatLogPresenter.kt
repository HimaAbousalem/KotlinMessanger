package com.example.abousalem.messengerapp.view.chat_log

import com.example.abousalem.messengerapp.base.BasePresenter
import com.example.abousalem.messengerapp.model.ChatMessage
import com.example.abousalem.messengerapp.model.DataManager
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError

class ChatLogPresenter<V: ChatLogMvpView>: BasePresenter<V>, ChatLogMvpPresenter<V>{

    constructor(dataManager: DataManager) : super(dataManager)

    override fun sendToId(toId: String) {
        getDataManager().getFromId(toId)
    }

    override fun retrieveAllMessages() {
        getDataManager().fetchMessages().addChildEventListener(object :ChildEventListener{

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(ChatMessage::class.java)
                if(chatMessage!=null) {
                    getMvpView().addObjectToAdapter(chatMessage,getDataManager().firebaseHelper.toId!!)
                }
            }
            override fun onChildRemoved(p0: DataSnapshot) {   }
            override fun onCancelled(p0: DatabaseError) { }
            override fun onChildMoved(p0: DataSnapshot, p1: String?) {  }
            override fun onChildChanged(p0: DataSnapshot, p1: String?) {   }
        })
    }

    override fun saveMessagesToDatabase(chatMessage: ChatMessage) {
        getDataManager().pushMessages(chatMessage).addOnSuccessListener {
            getMvpView().clearEditText()
        }
    }

}