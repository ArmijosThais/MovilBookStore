package com.example.elgatonegro.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.elgatonegro.model.Pedido

@Dao
interface PedidoDao {
    @Insert
    fun insert(pedido: Pedido):Long
    @Update
    fun update(pedido: Pedido)
    @Delete
    fun delete(pedido: Pedido)
    @Query("select * from tblPedidos order by id")
    fun getPedido(): LiveData<List<Pedido>>
    @Query("select * from tblPedidos where usuario=:idInput")
    fun getPedidoByUsuario(idInput:String): List<Pedido>
    @Query("select * from tblPedidos order by id DESC")
    fun getLastPedido(): List<Pedido>
    @Query("update tblPedidos set usuario=:usInput, direccion_entrega=:dirInput where usuario='user'")
    fun updateDatosPedido(usInput:String, dirInput:String)
}