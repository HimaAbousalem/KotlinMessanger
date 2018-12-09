package com.example.abousalem.messengerapp.model

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask

class DataManager: ModelPresenter{

    val firebaseHelper= FirebaseHelper()

    fun getUid(): String {
        return firebaseHelper.getAuth().uid!!
    }
    fun registerUser(email: String, password: String): Task<AuthResult> {
        return firebaseHelper.createNewUser(email,password)
    }
    fun loginUser(email: String, password: String): Task<AuthResult> {
        return firebaseHelper.signInUser(email, password)
    }
    fun IsuserLoggedIn(): Boolean {
        return firebaseHelper.isUserSignIn()
    }
    fun getUserImageRef(): StorageReference {
        return firebaseHelper.saveUserImageRef()
    }
    fun getSaveUserDataRef(user: User): Task<Void> {
        return firebaseHelper.saveUserToDatabaseRef(user)
    }
    fun getUsersRef(): DatabaseReference {
        return firebaseHelper.getAllUsersRef()
    }
    fun getCurrentUser(): DatabaseReference {
        return firebaseHelper.currentUser()
    }
    fun getLatestMessages(): DatabaseReference {
        return firebaseHelper.latestMessages()
    }
    override fun getFromId(toId: String) {
        firebaseHelper.toId = toId
    }
    fun fetchMessages(): DatabaseReference {
        return firebaseHelper.loadChatMessage()
    }
    fun pushMessages(chatMessage: ChatMessage): Task<Void> {
        return firebaseHelper.pushMessage(chatMessage)
    }
}
