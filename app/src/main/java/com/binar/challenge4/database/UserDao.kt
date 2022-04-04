package com.binar.challenge4.database

import androidx.room.*
import com.binar.challenge4.data.User

@Dao
interface UserDao {

    @Query("SELECT * FROM User WHERE id = :id")
    fun login(id: Int):User

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User):Long

}