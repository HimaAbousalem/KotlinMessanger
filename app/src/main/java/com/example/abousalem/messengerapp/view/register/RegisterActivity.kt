package com.example.abousalem.messengerapp.view.register

import android.app.Activity
import android.content.Context
import android.content.Intent

import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.example.abousalem.messengerapp.view.login.LoginActivity
import com.example.abousalem.messengerapp.view.latest_messages.LatestMessagesActivity
import com.example.abousalem.messengerapp.R
import com.example.abousalem.messengerapp.base.BaseActivity
import com.example.abousalem.messengerapp.model.DataManager
import com.example.abousalem.messengerapp.model.User
import com.example.abousalem.messengerapp.view.dialog_view.DialogView
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity: BaseActivity(), RegisterMvpView {


    val logTag = "RegisterActivity"
    var dialogView: DialogView? =null
    var registerPresenter: RegisterPresenter<RegisterMvpView>?= null

    companion object {
        fun startIntent(context: Context): Intent{
            val intent = Intent(context,RegisterActivity::class.java)
            return intent
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        dialogView = DialogView(this)
        val dataManager = DataManager()
        registerPresenter = RegisterPresenter(dataManager)
        registerPresenter!!.onAttach(this)
        register_button_register.setOnClickListener {
            processRegister()
        }
        already_have_account_text_view_register.setOnClickListener {
           registerPresenter!!.openLoginActivity()
        }
        select_photo_btn_register.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type ="image/*"
            startActivityForResult(intent, 0)
        }
    }
    var selectedPhoto: Uri? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==0 && resultCode== Activity.RESULT_OK&&data != null){
            selectedPhoto = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,selectedPhoto)
            select_photo_image_view_register.setImageBitmap(bitmap)
            select_photo_btn_register.alpha = 0f
        }
    }
    private fun processRegister() {
        dialogView!!.showDialog()
        val email = email_edittext_register.text.toString()
        val password = password_edittext_register.text.toString()
        val userName = user_name_edittext_register.text.toString()
        if(userName.isEmpty()||email.isEmpty()||password.isEmpty()||selectedPhoto == null){
            dialogView!!.hideDialog()
            Toast.makeText(this, "please enter text in username/email/psw!",Toast.LENGTH_SHORT).show()
            return
        }
        registerPresenter!!.registerToFirebase(email,password)
    }

    override fun openLoginActivity() {
        val intent = LoginActivity.startIntent(this)
        startActivity(intent)
    }

    override fun openLatestMessagesActivity() {
        dialogView!!.hideDialog()
        val intent = LatestMessagesActivity.startIntent(this)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    override fun onRegisterSuccess(uid : String) {
        Toast.makeText(this,"Successfully Registered!",Toast.LENGTH_SHORT).show()
        Log.d(logTag,"Successfully login")
        registerPresenter!!.saveUserImageToFirebase(selectedPhoto!!)

    }

    override fun onRegisterFail(message: String) {
        dialogView!!.hideDialog()
        Toast.makeText(this,"Registeration Failed!!",Toast.LENGTH_SHORT).show()

    }
    override fun onSaveImageSuccess(uid: String, profileImage: String) {
        val user = User(uid, user_name_edittext_register.text.toString(),profileImage)
        registerPresenter!!.saveUserToFirebase(user)
    }

}
