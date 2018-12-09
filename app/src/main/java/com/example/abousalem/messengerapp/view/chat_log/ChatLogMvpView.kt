package com.example.abousalem.messengerapp.view.chat_log

import com.example.abousalem.messengerapp.base.MvpView
import com.example.abousalem.messengerapp.model.ChatMessage

interface ChatLogMvpView: MvpView {
    fun addObjectToAdapter(chatMessage: ChatMessage, toId:String)
    fun clearEditText()

}