package com.example.abousalem.messengerapp.base

interface MvpPresenter<in V : MvpView> {

    fun onAttach(mvpView: V)
    fun detachView()
}
