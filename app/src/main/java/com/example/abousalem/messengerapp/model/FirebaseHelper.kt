package com.example.abousalem.messengerapp.model

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.util.*

class FirebaseHelper{
    private var mDatabase: FirebaseDatabase? = null
    private var mAuth: FirebaseAuth? = null
    var toId: String? = null

    private fun getDatabse(): FirebaseDatabase{
        if(mDatabase== null){
            mDatabase = FirebaseDatabase.getInstance()
        }
        return mDatabase as FirebaseDatabase
    }

    fun getAuth():FirebaseAuth{
        if(mAuth == null){
            mAuth = FirebaseAuth.getInstance()
        }
        return mAuth as FirebaseAuth
    }

    fun isUserSignIn():Boolean{
        if (getAuth().uid == null){
            return false
        }
        return true
    }

    fun createNewUser(email: String, password: String): Task<AuthResult> {
        return getAuth().createUserWithEmailAndPassword(email,password)
    }

    fun signInUser(email: String, password: String): Task<AuthResult>{
        return getAuth().signInWithEmailAndPassword(email,password)
    }

    fun logout() {
        getAuth().signOut()
    }

    fun saveUserImageRef(): StorageReference {
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
        return ref
    }

    fun saveUserToDatabaseRef(user: User): Task<Void> {
        return getDatabse().getReference("/users/${getAuth().uid}").setValue(user)
    }

    fun getAllUsersRef(): DatabaseReference {
        return getDatabse().getReference("/users/")
    }

    fun currentUser(): DatabaseReference {
        return getDatabse().getReference("/users/${getAuth().uid}")
    }
    fun latestMessages(): DatabaseReference {
        return getDatabse().getReference("/latest-messages/${getAuth().uid}")
    }
    fun loadChatMessage(): DatabaseReference {
        return getDatabse().getReference("/users-messages/${getAuth().uid}/$toId")
    }
    fun pushMessage(chatMessage: ChatMessage): Task<Void> {
        val ref =  getDatabse().getReference("/users-messages/${getAuth().uid}/$toId").push()
        chatMessage.id = ref.key!!
        getDatabse().getReference("/users-messages/$toId/${getAuth().uid}").push().setValue(chatMessage)
        FirebaseDatabase.getInstance().getReference("/latest-messages/${getAuth().uid}/$toId").setValue(chatMessage)
        FirebaseDatabase.getInstance().getReference("/latest-messages/$toId/${getAuth().uid}").setValue(chatMessage)
        return ref.setValue(chatMessage)
    }

}