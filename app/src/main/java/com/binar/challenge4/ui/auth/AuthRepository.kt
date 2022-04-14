package com.binar.challenge4.ui.auth

import android.content.Context
import com.binar.challenge4.data.User
import com.binar.challenge4.database.MyDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class AuthRepository(private val context: Context) {

    private var myDatabase = MyDatabase.getInstance(context)

    suspend fun login(email: String, password: String): User? = coroutineScope {
        return@coroutineScope myDatabase?.userDao()?.login(email, password)
    }

    suspend fun register(user: User): Long? = coroutineScope {
        return@coroutineScope myDatabase?.userDao()?.insertUser(user)
    }


    suspend fun checkEmailIfExist(email: String):User? = coroutineScope {
        return@coroutineScope myDatabase?.userDao()?.checkEmailExist(email)
    }


}