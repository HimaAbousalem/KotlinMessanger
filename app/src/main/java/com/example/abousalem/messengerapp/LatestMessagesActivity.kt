package com.example.abousalem.messengerapp

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.example.abousalem.messengerapp.Authentication.RegisterActivity
import com.example.abousalem.messengerapp.model.ChatMessage
import com.example.abousalem.messengerapp.model.User
import com.example.abousalem.messengerapp.view.LatestMessageRow
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_latest_messages.*
import kotlinx.android.synthetic.main.latest_message_row.view.*
import kotlinx.android.synthetic.main.user_row_new_message.view.*

class LatestMessagesActivity : AppCompatActivity() {

    companion object {
        var currentUser: User? = null
        val uid = FirebaseAuth.getInstance().uid
        val TAG = "LatestMessages"
    }
    var adapter = GroupAdapter<ViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_latest_messages)
        setupToolbar()
        recycler_view_latest_message.adapter = adapter
        recycler_view_latest_message.addItemDecoration(DividerItemDecoration(this,DividerItemDecoration.VERTICAL))
        adapter.setOnItemClickListener { item, _ ->
            Log.d(TAG, "item Clicked")
            val intent = Intent(this, ChatLogActivity::class.java)
            val row = item as LatestMessageRow
            intent.putExtra(NewMessageActivity.USER_KEY, row.chatPartnerUser)
            startActivity(intent)
        }
        verifyUserLoggedIn()
        fetchCurrentUser()
        listenForLatestMessages()
    }


    private fun setupToolbar() {
        setSupportActionBar(toolbar_latest_messages)
        supportActionBar?.title = "Messanger"
        toolbar_latest_messages.setTitleTextColor(Color.BLACK)
    }

    val latestMessagesMap = HashMap<String, ChatMessage>()
    private fun refreshRecyclerViewAdapter(){
        adapter.clear()
        latestMessagesMap.values.forEach {
            adapter.add(LatestMessageRow(it))
        }
    }

    private fun listenForLatestMessages() {
        Log.d(TAG,"$uid\n ${FirebaseAuth.getInstance().uid}")
        val ref = FirebaseDatabase.getInstance().getReference("/latest-messages/$uid")
        ref.addChildEventListener(object :ChildEventListener{
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(ChatMessage::class.java)?: return
                    latestMessagesMap[p0.key!!] = chatMessage
                    refreshRecyclerViewAdapter()
            }
            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(ChatMessage::class.java) ?: return
                    latestMessagesMap[p0.key!!] = chatMessage
                    refreshRecyclerViewAdapter()
            }

            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }
            override fun onChildRemoved(p0: DataSnapshot) {
            }
        })
    }

    private fun fetchCurrentUser() {
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onDataChange(p0: DataSnapshot) {
                currentUser =p0.getValue(User::class.java)
            }
        })
    }

    private fun verifyUserLoggedIn() {
        if(uid == null ) {
            val intent = Intent(this, RegisterActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.new_message_menu_item ->{
                val intent = Intent(this, NewMessageActivity::class.java)
                startActivity(intent)
            }
            R.id.sign_out_menu_item ->{
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, RegisterActivity::class.java)
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

