package com.example.abousalem.messengerapp.view.dialog_view

import android.app.Activity
import android.app.Dialog
import android.view.Window
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget
import com.example.abousalem.messengerapp.R
import kotlinx.android.synthetic.main.my_custom_dialoge.*

class DialogView(var activity: Activity) {
    
    var dialog: Dialog? =null

    fun showDialog() {
        dialog = Dialog(activity)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCancelable(false)
        dialog!!.setContentView(R.layout.my_custom_dialoge)
        val gifImageView = dialog!!.custom_loading_imageView
        val imageViewTarget = GlideDrawableImageViewTarget(gifImageView)
        Glide.with(activity)
                .load(R.drawable.loading)
                .placeholder(R.drawable.loading)
                .centerCrop()
                .crossFade()
                .into(imageViewTarget)
        dialog!!.show()
    }
    
    fun hideDialog() {
        dialog!!.dismiss()
    }
}
