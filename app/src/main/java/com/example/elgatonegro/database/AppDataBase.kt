package com.example.elgatonegro.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.elgatonegro.FragmentShopping
import com.example.elgatonegro.model.Book
import com.example.elgatonegro.model.DetallePedido
import com.example.elgatonegro.model.Pedido
import com.example.elgatonegro.model.User

@Database(entities = [Book::class,Pedido::class, DetallePedido::class, User::class], version = 1, exportSchema = false)
abstract class AppDataBase:RoomDatabase() {
    //Definir el DAO a utilizar
    abstract fun bookDao():BookDao
    abstract fun pedidoDao():PedidoDao
    abstract fun detallePedidoDao():DetallePedidoDao
    abstract fun userDao():UserDao

    //Definir la instancia de la base de datos
    //companion ibjetct es un objeto al que se puede acceder una sola vez, es decir se puede istanciar una sola vez
    companion object {
        var instancia:AppDataBase? = null

        //manejar la instancia de la base
        fun getInstancia(context: Context):AppDataBase{
            if (instancia==null){
                instancia = Room.databaseBuilder(
                    context,
                    AppDataBase::class.java,
                    "bdLibGatoNegro"
                ).allowMainThreadQueries().build()
            }
            return instancia as AppDataBase
        }
    }
}