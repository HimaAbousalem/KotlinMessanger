package com.example.abousalem.messengerapp.view.latest_messages


import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.example.abousalem.messengerapp.view.new_message.NewMessageActivity
import com.example.abousalem.messengerapp.R
import com.example.abousalem.messengerapp.base.BaseActivity

import com.example.abousalem.messengerapp.view.register.RegisterActivity
import com.example.abousalem.messengerapp.model.ChatMessage
import com.example.abousalem.messengerapp.model.DataManager
import com.example.abousalem.messengerapp.model.User
import com.example.abousalem.messengerapp.view.chat_log.ChatLogActivity
import com.google.firebase.auth.FirebaseAuth
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_latest_messages.*
class LatestMessagesActivity : BaseActivity(), LatestMessageMvpView {


    companion object {
        var currentUser: User? = null
        val TAG = "LatestMessages"
        fun startIntent(context: Context): Intent{
            return Intent(context, LatestMessagesActivity::class.java)
        }
    }

    var adapter = GroupAdapter<ViewHolder>()
    var latestMessagePresenter: LatestMessagePresenter<LatestMessageMvpView>?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_latest_messages)
        val dataManager = DataManager()
        latestMessagePresenter = LatestMessagePresenter(dataManager)
        latestMessagePresenter!!.onAttach(this)
        latestMessagePresenter!!.IsUserLoggedIn()
        setupToolbar()
        recycler_view_latest_message.adapter = adapter
        recycler_view_latest_message.addItemDecoration(DividerItemDecoration(this,DividerItemDecoration.VERTICAL))
        adapter.setOnItemClickListener { item, _ ->
            Log.d(TAG, "item Clicked")
            openChatLogActivity(item)
        }
    }


    private fun setupToolbar() {
        setSupportActionBar(toolbar_latest_messages)
        supportActionBar?.title = "Messanger"
        toolbar_latest_messages.setTitleTextColor(Color.BLACK)
    }


    override fun getCurrentUser(user: User) {
        currentUser = user
        latestMessagePresenter!!.listenForLatestMessages()
    }
    override fun openChatLogActivity(item: Item<ViewHolder>) {
        val intent = ChatLogActivity.startIntent(this)
        val row = item as LatestMessageRow
        intent.putExtra(NewMessageActivity.USER_KEY, row.chatPartnerUser)
        startActivity(intent)
    }

    override fun refreshRecyclerView(latestMessagesMap: HashMap<String, ChatMessage>) {
        adapter.clear()
        latestMessagesMap.values.forEach {
            adapter.add(LatestMessageRow(it))
        }
    }

    override fun openRegisterActivity() {
        val intent = RegisterActivity.startIntent(this)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.new_message_menu_item ->{
                val intent = NewMessageActivity.startIntent(this)
                startActivity(intent)
            }
            R.id.sign_out_menu_item ->{
                FirebaseAuth.getInstance().signOut()
                val intent = RegisterActivity.startIntent(this)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }
}

