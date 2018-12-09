package com.example.abousalem.messengerapp.view.chat_log

import com.example.abousalem.messengerapp.base.MvpPresenter
import com.example.abousalem.messengerapp.model.ChatMessage

interface ChatLogMvpPresenter<in V:ChatLogMvpView>: MvpPresenter<V>{
    fun sendToId(toId: String)
    fun retrieveAllMessages()
    fun saveMessagesToDatabase(chatMessage: ChatMessage)
}