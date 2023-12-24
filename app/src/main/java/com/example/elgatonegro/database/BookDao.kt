package com.example.elgatonegro.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.elgatonegro.model.Book

@Dao
interface BookDao {
    //Sirve para definir el CRUD
    @Insert
    fun insert(book:Book):Long
    @Update
    fun update(book:Book)
    @Delete
    fun delete(book:Book)
    @Query("select * from tblBooks order by titulo")
    fun getBooks(): LiveData<List<Book>>
    @Query("select * from tblBooks where id=:idInput")
    fun getBookById(idInput:Int): List<Book>
    @Query("select * from tblBooks where seleccionado=1")
    fun getSelectedBooks(): LiveData<List<Book>>
    @Query("select * from tblBooks where titulo like '%'||:title||'%' order by id")
    fun getBooksByTitle(title:String): LiveData<List<Book>>
    @Query("select * from tblBooks where genero=:gender order by id")
    fun getBooksByGender(gender:String): LiveData<List<Book>>
    @Query("update tblBooks set cantidad=:cantidad where id=:idInput")
    fun updateCantidadByIdLibro(idInput:Int, cantidad:Int)
    @Query("update tblBooks set seleccionado=0")
    fun cleanSelectedBooks()

}