package com.example.abousalem.messengerapp.view.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.example.abousalem.messengerapp.view.latest_messages.LatestMessagesActivity
import com.example.abousalem.messengerapp.R
import com.example.abousalem.messengerapp.base.BaseActivity
import com.example.abousalem.messengerapp.model.DataManager
import com.example.abousalem.messengerapp.view.dialog_view.DialogView
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity(), LoginMvpView {
    var loginPresenter: LoginPresenter<LoginMvpView>? = null
    var dialogView: DialogView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val dataManager = DataManager()
        loginPresenter = LoginPresenter(dataManager)
        loginPresenter!!.onAttach(this)
        login_button_login.setOnClickListener {
            performLogin()
        }
        back_to_register_screen_textview_login.setOnClickListener {
            finish()
        }
    }

    private fun performLogin() {
        dialogView = DialogView(this)
        dialogView!!.showDialog()
        val email = email_editText_login.text.toString()
        val password = password_editText_login.text.toString()
        if(email.isEmpty()||password.isEmpty()){
            dialogView!!.hideDialog()
            return
        }
        loginPresenter!!.loginToFirebase(email,password)
    }
    override fun openLatestMessagesActivity() {
        val intent = LatestMessagesActivity.startIntent(this)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    override fun onLoginSuccess() {
        dialogView!!.hideDialog()
        openLatestMessagesActivity()
        Toast.makeText(this, "Login Successfully!", Toast.LENGTH_SHORT).show()
    }

    override fun onLoginFail(message: String) {
        dialogView!!.hideDialog()
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        fun startIntent(context: Context): Intent{
            return Intent(context, LoginActivity::class.java)
        }
    }
}
