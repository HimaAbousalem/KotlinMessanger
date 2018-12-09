package com.example.abousalem.messengerapp.base

import com.example.abousalem.messengerapp.model.DataManager

open class BasePresenter<V : MvpView>(private val mDataManager: DataManager) : MvpPresenter<V>
{
    private var mMvpView: V? = null

    override fun onAttach(mvpView: V) {
        mMvpView = mvpView
    }

    override fun detachView() {
        mMvpView = null
    }

    fun getDataManager():DataManager{
        return mDataManager
    }
    fun getMvpView(): V{
        return mMvpView!!
    }
}
