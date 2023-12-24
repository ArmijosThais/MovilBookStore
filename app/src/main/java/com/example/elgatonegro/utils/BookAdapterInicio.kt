package com.example.elgatonegro.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.elgatonegro.R
import com.example.elgatonegro.databinding.ItemBookInicioBinding
import com.example.elgatonegro.model.Book
import com.example.elgatonegro.utils.BookAdapterInicio.BookAdapterInicioViewHolder
import com.squareup.picasso.Picasso

class BookAdapterInicio (var books:List<Book> = emptyList()): RecyclerView.Adapter<BookAdapterInicioViewHolder>(){
    lateinit var  setOnClickBook:(Book) -> Unit

    //Crear el viewHolder
    inner class BookAdapterInicioViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private var binding: ItemBookInicioBinding = ItemBookInicioBinding.bind(itemView)

        fun bind(book:Book) =  with(binding){
            txtTituloInicio.setText(book.titulo)
            txtPrecioInicio.setText("$ "+String.format("%.2f",book.precio))
            Picasso.get().load(book.url).error(R.drawable.ic_launcher_background).into(imgPortadaInicio)

            root.setOnClickListener{
                setOnClickBook(book)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookAdapterInicioViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_book_inicio,parent,false)
        return BookAdapterInicioViewHolder(view)
    }

    //esta funcion se va a repetir tantas veces como elementos existen en la lista
    override fun onBindViewHolder(holder: BookAdapterInicioViewHolder, position: Int) {
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