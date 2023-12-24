package com.example.elgatonegro.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.elgatonegro.model.Book
import com.example.elgatonegro.model.DetallePedido

@Dao
interface DetallePedidoDao {
    @Insert
    fun insert(detallePedido: DetallePedido):Long
    @Update
    fun update(detallePedido: DetallePedido)
    @Delete
    fun delete(detallePedido: DetallePedido)
    @Query("select * from tblDetallePedidos where pedido=-1 order by pedido")
    fun getDetallesPedidos(): LiveData<List<DetallePedido>>
    @Query("select * from tblDetallePedidos where pedido=:ped")
    fun getDetalleByIdPedido(ped:Int): List<DetallePedido>
    @Query("select cantidad from tblDetallePedidos where libro=:idInput and pedido=-1")
    fun getCantidadDetalleByIdLibro(idInput:Int): List<Int>
    @Query("update tblDetallePedidos set cantidad=:cantidad, subtotal=:cantidad*precio_unitario  where pedido=-1 and libro=:idInput")
    fun updateValuesDetalleByIdLibro(idInput:Int, cantidad:Int)
    @Query("delete from tblDetallePedidos where pedido=-1 and libro=:idInput")
    fun deleteByIdLibro(idInput:Int)
    @Query("select sum(subtotal) from tblDetallePedidos where pedido=-1")
    fun getSumSubtotales():Float
    @Query("update tblDetallePedidos set pedido=:pedido where pedido=-1")
    fun asignarNuevoPedidoDetalles(pedido:Int)

}