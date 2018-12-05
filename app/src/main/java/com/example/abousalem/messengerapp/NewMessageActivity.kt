package com.example.abousalem.messengerapp

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.abousalem.messengerapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_latest_messages.*
import kotlinx.android.synthetic.main.activity_new_message.*
import kotlinx.android.synthetic.main.user_row_new_message.view.*

class NewMessageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)
        setupToolbar()
        fetchUsers()
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

    companion object {
        val USER_KEY = "USER_KEY"
    }
    private fun fetchUsers() {
        val ref = FirebaseDatabase.getInstance().getReference("/users/")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()
                val uid = LatestMessagesActivity.uid
                Log.d(USER_KEY,"$uid\n ${FirebaseAuth.getInstance().uid}")
                p0.children.forEach {
                    val user = it.getValue(User::class.java)
                    Log.d("users","${it.value}")
                    if(user != null&& user.uid!=uid ) {
                           adapter.add(UserItem(user))
                    }
                }
                adapter.setOnItemClickListener { item, view ->
                    val userItem = item as UserItem
                    val intent = Intent(view.context, ChatLogActivity::class.java)
                    intent.putExtra(USER_KEY, userItem.user)
                    startActivity(intent)
                    finish()
                }
                recycle_view_new_message.adapter = adapter
               }

            override fun onCancelled(p0: DatabaseError) {
              }

        })
    }
}

class UserItem(val user: User): Item<ViewHolder>(){
    override fun getLayout(): Int {
        return R.layout.user_row_new_message
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.user_name_text_view_new_message.text = user.username
        Picasso.get().load(user.profileImageUrl).into(viewHolder.itemView.user_img_view_new_message)
    }

}
