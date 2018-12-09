package com.example.abousalem.messengerapp.view.latest_messages

import com.example.abousalem.messengerapp.R
import com.example.abousalem.messengerapp.model.ChatMessage
import com.example.abousalem.messengerapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.latest_message_row.view.*

class LatestMessageRow(val chatMessage: ChatMessage): Item<ViewHolder>(){
    var chatPartnerUser: User? = null
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.message_textview_latest_message.text = chatMessage.text
        val partnerId: String
        if(chatMessage.fromId == FirebaseAuth.getInstance().uid){
            partnerId = chatMessage.toId
        }else{
            partnerId = chatMessage.fromId
        }
        val ref = FirebaseDatabase.getInstance().getReference("/users/$partnerId")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                chatPartnerUser =p0.getValue(User::class.java)
                viewHolder.itemView.user_name_latest_messages.text = chatPartnerUser?.username
                Picasso.get().load(chatPartnerUser?.profileImageUrl).into(viewHolder.itemView.user_imageView_latest_message)
            }
            override fun onCancelled(p0: DatabaseError) {
            }
        })
    }
    override fun getLayout(): Int {
        return R.layout.latest_message_row
    }

}