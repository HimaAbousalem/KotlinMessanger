package com.example.abousalem.messengerapp.view.chat_log

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.abousalem.messengerapp.view.latest_messages.LatestMessagesActivity
import com.example.abousalem.messengerapp.view.new_message.NewMessageActivity
import com.example.abousalem.messengerapp.R
import com.example.abousalem.messengerapp.base.BaseActivity
import com.example.abousalem.messengerapp.model.ChatMessage
import com.example.abousalem.messengerapp.model.DataManager
import com.example.abousalem.messengerapp.model.User
import com.example.abousalem.messengerapp.view.latest_messages.LatestMessagesActivity.Companion.currentUser
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*

class ChatLogActivity : BaseActivity(), ChatLogMvpView {


    companion object {
        val TAG = "CHAT_LOG"
        fun startIntent(context: Context): Intent{
            return Intent(context, ChatLogActivity::class.java)
        }
    }
    private var userTo: User? = null
    private val adapter = GroupAdapter<ViewHolder>()
    private var chatLogPresenter: ChatLogPresenter<ChatLogMvpView>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)
        userTo = getUserIntent()
        setupToolbar()
        val dataManager = DataManager()
        chatLogPresenter = ChatLogPresenter(dataManager)
        chatLogPresenter!!.onAttach(this)
        chatLogPresenter!!.sendToId(userTo!!.uid)
        chatLogPresenter!!.retrieveAllMessages()
        recyclerview_chat_log.adapter = adapter
        send_button_chat_log.setOnClickListener {
            performMessage()
        }
    }
    @SuppressLint("NewApi", "PrivateResource")
    private fun setupToolbar() {
        setSupportActionBar(toolbar_chat_log)
        supportActionBar?.title= userTo?.username
        toolbar_chat_log.setTitleTextColor(Color.BLACK)
        toolbar_chat_log.navigationIcon = getDrawable(R.drawable.abc_ic_ab_back_material)
        toolbar_chat_log.setNavigationOnClickListener {
            finish()
           val intent = Intent(this, LatestMessagesActivity::class.java)
           intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

    }
    override fun addObjectToAdapter(chatMessage: ChatMessage, toId: String) {
        if(chatMessage.toId == toId){
            adapter.add(ChatItems.ChatFromItem(chatMessage,LatestMessagesActivity.currentUser!!))
            adapter.notifyDataSetChanged()
        }else{
            adapter.add(ChatItems.ChatToItem(chatMessage,userTo!!))
            adapter.notifyDataSetChanged()
        }
    }
    private fun getUserIntent(): User {
        return intent.getParcelableExtra(NewMessageActivity.USER_KEY)
    }
    override fun clearEditText() {
        Log.d(TAG,"added successfully")
        editText_chat_log.text.clear()
        recyclerview_chat_log.scrollToPosition(adapter.itemCount - 1)
    }
    private fun performMessage() {
        val messageText = editText_chat_log.text.toString()
        val fromId = LatestMessagesActivity.currentUser!!.uid
        val toId =userTo!!.uid
        if(messageText.isEmpty())return
        val chatMessage = ChatMessage("", messageText, fromId, toId, System.currentTimeMillis()/1000)
        chatLogPresenter!!.saveMessagesToDatabase(chatMessage)
    }
}

