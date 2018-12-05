package com.example.abousalem.messengerapp.view

import com.example.abousalem.messengerapp.R
import com.example.abousalem.messengerapp.model.ChatMessage
import com.example.abousalem.messengerapp.model.User
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.chat_from_row.view.*
import kotlinx.android.synthetic.main.chat_to_row.view.*

class ChatItems{

    class ChatFromItem(val chatMessage: ChatMessage, val userFrom: User): Item<ViewHolder>() {
        override fun getLayout(): Int {
            return R.layout.chat_from_row
        }

        override fun bind(viewHolder: ViewHolder, position: Int) {
            viewHolder.itemView.textView_from_row.text =chatMessage.text
            Picasso.get().load(userFrom.profileImageUrl).into(viewHolder.itemView.imageView_from_row)
        }
    }
    class ChatToItem(val chatMessage: ChatMessage, val userTo: User): Item<ViewHolder>() {
        override fun getLayout(): Int {
            return R.layout.chat_to_row
        }

        override fun bind(viewHolder: ViewHolder, position: Int) {
            viewHolder.itemView.textView_to_row.text =chatMessage.text
            Picasso.get().load(userTo.profileImageUrl).into(viewHolder.itemView.imageView_to_row)
        }
    }

}