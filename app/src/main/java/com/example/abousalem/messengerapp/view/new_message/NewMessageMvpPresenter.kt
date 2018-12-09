package com.example.abousalem.messengerapp.view.new_message

import com.example.abousalem.messengerapp.base.MvpPresenter

interface  NewMessageMvpPresenter<in V:NewMessageMvpView>: MvpPresenter<V>{
    fun fetchUsers()
}