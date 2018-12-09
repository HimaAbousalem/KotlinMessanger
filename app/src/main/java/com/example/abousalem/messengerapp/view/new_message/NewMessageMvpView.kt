package com.example.abousalem.messengerapp.view.new_message

import com.example.abousalem.messengerapp.base.MvpView
import com.example.abousalem.messengerapp.model.User
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder

interface NewMessageMvpView: MvpView{
    fun openChatLogActivity(item: Item<ViewHolder>)
    fun updateNewMessageAdapter(users: ArrayList<User>)

}