package com.example.elgatonegro.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "tblBooks")
data class Book (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "titulo")
    val titulo:String,
    @ColumnInfo(name = "autor")
    val autor:String,
    @ColumnInfo(name = "precio")
    val precio:Float,
    @ColumnInfo(name = "descripcion")
    val descripcion:String,
    @ColumnInfo(name = "url")
    val url:String,
    @ColumnInfo(name = "genero")
    val genero:String,
    @ColumnInfo(name = "seleccionado")
    val seleccionado:Int,
    @ColumnInfo(name = "cantidad")
val cantidad: Int
):Serializable

@Entity(tableName = "tblUsers")
data class User(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "cedula")
    val cedula: String,
    @ColumnInfo(name = "nombre")
    val nombre:String,
    @ColumnInfo(name = "apellido")
    val apellido:String,
    @ColumnInfo(name = "telefono")
    val telefono:String,
    @ColumnInfo(name = "email")
    val email:String,
    @ColumnInfo(name = "direccion")
    val direccion:String,
    @ColumnInfo(name = "fecha_nacimiento")
    val fecha_nacimiento:String,
    @ColumnInfo(name = "clave")
    val clave:String
):Serializable

@Entity(tableName = "tblPedidos")
data class Pedido (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "fecha_pedido")
    val fecha_pedido:String,
    @ColumnInfo(name = "usuario")
    val usuario:String,
    @ColumnInfo(name = "direccion_entrega")
    val direccion_entrega:String,
    @ColumnInfo(name = "total")
    val total:Float
):Serializable

@Entity(tableName = "tblDetallePedidos")
data class DetallePedido (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "pedido")
    val pedido: Int,
    @ColumnInfo(name = "libro")
    val libro: Int,
    @ColumnInfo(name = "cantidad")
    val cantidad: Int,
    @ColumnInfo(name = "precio_unitario")
    val precio_unitario:Float,
    @ColumnInfo(name = "subtotal")
    val subtotal:Float
):Serializable