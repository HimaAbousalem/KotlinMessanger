package com.example.abousalem.messengerapp

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.abousalem.messengerapp.model.ChatMessage
import com.example.abousalem.messengerapp.model.User
import com.example.abousalem.messengerapp.view.ChatItems
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*
import kotlinx.android.synthetic.main.chat_from_row.view.*
import kotlinx.android.synthetic.main.chat_to_row.view.*

class ChatLogActivity : AppCompatActivity() {

    companion object {
        val TAG = "CHAT_LOG"
    }
    var userTo: User? = null
    val adapter = GroupAdapter<ViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)
        getUserIntent()
        userTo = getUserIntent()
        setupToolbar()
        recyclerview_chat_log.adapter = adapter
        listenOfMessages()
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

    private fun listenOfMessages() {
        val fromId = LatestMessagesActivity.uid
        val toId =userTo?.uid
        val ref = FirebaseDatabase.getInstance().getReference("/users-messages/$fromId/$toId")
        ref.addChildEventListener(object: ChildEventListener{
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(ChatMessage::class.java)
                if(chatMessage!=null) {
                    if (chatMessage.fromId.equals(fromId)){
                        val currentUser = LatestMessagesActivity.currentUser ?:return
                        adapter.add(ChatItems.ChatFromItem(chatMessage, currentUser))
                    }else{ adapter.add(ChatItems.ChatToItem(chatMessage, userTo!!))}
                }
            }
            override fun onCancelled(p0: DatabaseError) {}
            override fun onChildMoved(p0: DataSnapshot, p1: String?) {}
            override fun onChildChanged(p0: DataSnapshot, p1: String?) {}
            override fun onChildRemoved(p0: DataSnapshot) {}

        })
    }

    private fun getUserIntent(): User {
        return intent.getParcelableExtra(NewMessageActivity.USER_KEY)
    }

    private fun performMessage() {
        val messageText = editText_chat_log.text.toString()
        val fromId = LatestMessagesActivity.uid
        val toId =userTo!!.uid
        if(messageText.isEmpty() || fromId == null)return

        val ref = FirebaseDatabase.getInstance().getReference("/users-messages/$fromId/$toId").push()
        val toRef = FirebaseDatabase.getInstance().getReference("/users-messages/$toId/$fromId").push()
        val chatMessage = ChatMessage(ref.key!!, messageText, fromId, toId, System.currentTimeMillis()/1000)
        ref.setValue(chatMessage).addOnSuccessListener {
            Log.d(TAG,"added successfully")
            editText_chat_log.text.clear()
            recyclerview_chat_log.scrollToPosition(adapter.itemCount - 1)
        }
        toRef.setValue(chatMessage)

        val latestMessagesFromRef = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId/$toId")
        latestMessagesFromRef.setValue(chatMessage)

        val latestMessagesToRef = FirebaseDatabase.getInstance().getReference("/latest-messages/$toId/$fromId")
        latestMessagesToRef.setValue(chatMessage)
    }
}

