package com.example.abousalem.messengerapp.view.latest_messages

import com.example.abousalem.messengerapp.base.MvpView
import com.example.abousalem.messengerapp.model.ChatMessage
import com.example.abousalem.messengerapp.model.User
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder

interface LatestMessageMvpView: MvpView {
    fun openChatLogActivity(item: Item<ViewHolder>)
    fun refreshRecyclerView(latestMessagesMap: HashMap<String,ChatMessage>)
    fun openRegisterActivity()
    fun getCurrentUser(user: User)
}