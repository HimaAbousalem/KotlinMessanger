package com.example.abousalem.messengerapp.view.latest_messages

import com.example.abousalem.messengerapp.base.MvpPresenter
import com.example.abousalem.messengerapp.base.MvpView

interface LatestMessageMvpPresenter<in V:LatestMessageMvpView>: MvpPresenter<V> {
    fun IsUserLoggedIn()
    fun fetchCurrentUser()
    fun listenForLatestMessages()
}