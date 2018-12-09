package com.example.abousalem.messengerapp.view.new_message

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import com.example.abousalem.messengerapp.R
import com.example.abousalem.messengerapp.base.BaseActivity
import com.example.abousalem.messengerapp.model.DataManager
import com.example.abousalem.messengerapp.model.User
import com.example.abousalem.messengerapp.view.chat_log.ChatLogActivity
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_new_message.*

class NewMessageActivity : BaseActivity(), NewMessageMvpView {

    companion object {
        val USER_KEY = "USER_KEY"
        fun startIntent(context: Context): Intent{
            return Intent(context, NewMessageActivity::class.java)
        }
    }
    val adapter = GroupAdapter<ViewHolder>()
    var newMessagePresenter: NewMessagePresenter<NewMessageMvpView>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)
        val dataManager = DataManager()
        newMessagePresenter = NewMessagePresenter(dataManager)
        newMessagePresenter!!.onAttach(this)
        newMessagePresenter!!.fetchUsers()
        recycle_view_new_message.adapter = adapter
        adapter.setOnItemClickListener { item, view ->
            this.openChatLogActivity(item)
        }
        setupToolbar()
    }

    @SuppressLint("NewApi", "PrivateResource")
    private fun setupToolbar() {
        setSupportActionBar(toolbar_new_message)
        toolbar_new_message.navigationIcon = getDrawable(R.drawable.abc_ic_ab_back_material)
        supportActionBar?.title = "Select User"
        toolbar_new_message.setTitleTextColor(Color.BLACK)
        toolbar_new_message.setNavigationOnClickListener {
            finish()
        }
    }

    override fun openChatLogActivity(item: Item<ViewHolder>) {
        val userItem = item as UserItem
        val intent = ChatLogActivity.startIntent(this)
        intent.putExtra(USER_KEY, userItem.user)
        startActivity(intent)
        finish()
    }

    override fun updateNewMessageAdapter(users: ArrayList<User>) {
        users.forEach {
            adapter.add(UserItem(it))
        }

    }
}


