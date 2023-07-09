package com.example.newsplatform.daos

import com.example.newsplatform.models.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class UserDao {
    private val db = FirebaseFirestore.getInstance()
    private val usersCollection = db.collection(  "users")
    fun addUser(user: User?) {
        user?. let {
            GlobalScope.launch(Dispatchers.IO) {
                usersCollection.document (user.uid).set(it)
            }
}}}
