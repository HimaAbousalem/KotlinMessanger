package com.example.abousalem.messengerapp.Authentication

import android.app.Activity
import android.content.Intent

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.example.abousalem.messengerapp.LatestMessagesActivity
import com.example.abousalem.messengerapp.R
import com.example.abousalem.messengerapp.model.User
import com.example.abousalem.messengerapp.view.DialogView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*

class RegisterActivity : AppCompatActivity() {
    val logTag = "RegisterActivity"
    var dialogView:DialogView? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        dialogView = DialogView(this)
        register_button_register.setOnClickListener {
            processRegister()
        }
        already_have_account_text_view_register.setOnClickListener {
            //Go To Login Activity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
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
//            val BitmapDrawable = BitmapDrawable(bitmap)
//            select_photo_btn_register.setBackgroundDrawable(BitmapDrawable)
        }
    }
    private fun processRegister() {
        dialogView!!.showDialog()
        val email = email_edittext_register.text.toString()
        val password = password_edittext_register.text.toString()
        val userName = user_name_edittext_register.text.toString()
        if(userName.isEmpty()||email.isEmpty()||password.isEmpty()){
            Toast.makeText(this, "please enter text in username/email/psw!",Toast.LENGTH_SHORT).show()
            return
        }
        Log.d(logTag, "Email: $email")
        Log.d(logTag,"Password: $password")
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener{
                    if(!it.isSuccessful)return@addOnCompleteListener
                    Log.d(logTag,"Successfully login using uid: ${it.result!!.user.uid}")
                    uploadImageToFirebase()
                }
                .addOnFailureListener{
                    Toast.makeText(this,"Failed to Register beacause of :${it.message}",Toast.LENGTH_SHORT).show()
                }
    }

    private fun uploadImageToFirebase() {
        if(selectedPhoto==null) return

        val filename = UUID.randomUUID().toString()
        val ref =FirebaseStorage.getInstance().getReference("/images/$filename")
        ref.putFile(selectedPhoto!!)
                .addOnSuccessListener {
                    Log.d(logTag, "Image uploaded successfully: ${it.metadata?.path}")
                    ref.downloadUrl.addOnSuccessListener {
                        Log.d(logTag, "download Uri:$it")
                        saveUserToDatabase(it.toString())
                    }

                }.addOnFailureListener{
                    Log.d(logTag, "${it.message}")
                }
    }

    private fun saveUserToDatabase(selectedPhoto: String) {
        val uid = FirebaseAuth.getInstance().uid ?:""
        val user = User(uid, user_name_edittext_register.text.toString(),selectedPhoto)
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.setValue(user).addOnSuccessListener {
            Log.d(logTag, "Finally user saved successfully into database!")
            val intent = Intent(this, LatestMessagesActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            dialogView!!.hideDialog()
        }.addOnFailureListener{
            Log.d(logTag,"${it.message}")
        }
    }

}
