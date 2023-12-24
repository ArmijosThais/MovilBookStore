package com.example.elgatonegro.utils

import android.content.DialogInterface.OnClickListener
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.example.elgatonegro.R
import com.example.elgatonegro.database.AppDataBase
import com.example.elgatonegro.databinding.ItemBookBinding
import com.example.elgatonegro.model.Book
import com.example.elgatonegro.model.DetallePedido
import com.squareup.picasso.Picasso
import kotlin.concurrent.fixedRateTimer
import kotlin.properties.Delegates

class BookAdapter (var books:List<Book> = emptyList()): RecyclerView.Adapter<BookAdapter.BookAdapterViewHolder>(){
    //Funcion para manipular la edicion y eliminacion de registros
    lateinit var setOnClickListenerEliminar:(Book) -> Unit
    lateinit var setOnClickBook:(Book) -> Unit
    lateinit var setOnClickListenerSumar:(Book) -> Unit
    lateinit var setOnClickListenerRestar:(Book) -> Unit

//    lateinit var sum:Number

    //Crear el viewHolder
    inner class BookAdapterViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private var binding: ItemBookBinding = ItemBookBinding.bind(itemView)

        fun bind(book:Book) =  with(binding){
            txtTituloCarrito.setText(book.titulo)
            txtAutorCarrito.setText(book.autor)
            txtGeneroCarrito.setText(book.genero)
            txtPrecioCarrito.setText("$ "+String.format("%.2f",book.precio))
            txtCantidadCarrito.setText(book.cantidad.toString())
            Picasso.get().load(book.url).error(R.drawable.ic_launcher_background).into(imgPortadaCarrito)

            btnRestar.setOnClickListener{
                if(book.cantidad >1){
                    txtCantidadCarrito.setText((book.cantidad-1).toString())
                    setOnClickListenerRestar(book)
                }
            }

            btnSumar.setOnClickListener {
                    txtCantidadCarrito.setText((book.cantidad+1).toString())
                    setOnClickListenerSumar(book)
            }

            btnEliminarCarrito.setOnClickListener {
                setOnClickListenerEliminar(book)
            }

            root.setOnClickListener{
                setOnClickBook(book)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookAdapter.BookAdapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_book,parent,false)
        return BookAdapterViewHolder(view)
    }

    //esta funcion se va a repetir tantas veces como elementos existen en la lista
    override fun onBindViewHolder(holder: BookAdapterViewHolder, position: Int) {
        val book = books[position]
        holder.bind(book)
    }

    //esta funcion cuenta los elementos de la lista
    override fun getItemCount(): Int {
        return books.size
    }

    fun updateListBooks(books: List<Book>){
        this.books = books
        notifyDataSetChanged()
    }
}