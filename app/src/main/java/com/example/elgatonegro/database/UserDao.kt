package com.example.elgatonegro.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.elgatonegro.model.Book
import com.example.elgatonegro.model.User

@Dao
interface UserDao {
    //Sirve para definir el CRUD
    @Insert
    fun insert(user: User):Long
    @Update
    fun update(user: User)
    @Query("select * from tblUsers where email=:idInput ")
    fun getUserByEmail(idInput:String): User
    @Query("select * from tblUsers")
    fun getUsers(): List<User>
}