package com.example.abousalem.messengerapp.view.new_message

import com.example.abousalem.messengerapp.R
import com.example.abousalem.messengerapp.model.User
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.user_row_new_message.view.*

class UserItem(val user: User): Item<ViewHolder>(){
    override fun getLayout(): Int {
        return R.layout.user_row_new_message
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.user_name_text_view_new_message.text = user.username
        Picasso.get().load(user.profileImageUrl).into(viewHolder.itemView.user_img_view_new_message)
    }

}