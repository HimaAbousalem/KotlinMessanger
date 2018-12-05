package com.example.abousalem.messengerapp.Authentication

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.abousalem.messengerapp.LatestMessagesActivity
import com.example.abousalem.messengerapp.R
import com.example.abousalem.messengerapp.view.DialogView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        login_button_login.setOnClickListener {
            performLogin()
        }
        back_to_register_screen_textview_login.setOnClickListener {
            finish()
        }
    }

    private fun performLogin() {
        val dialogView = DialogView(this)
        dialogView.showDialog()
        val email = email_editText_login.text.toString()
        val password = password_editText_login.text.toString()
        if(email.isEmpty()||password.isEmpty())return
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
                .addOnCompleteListener{
                    if(!it.isSuccessful)return@addOnCompleteListener
                    else {
                        val intent = Intent(this, LatestMessagesActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        dialogView.hideDialog()
                    }
                }.addOnFailureListener{
                    Toast.makeText(this,"${it.message}",Toast.LENGTH_SHORT).show()
                }
    }
}
