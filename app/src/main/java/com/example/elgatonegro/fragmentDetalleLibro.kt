package com.example.elgatonegro

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.elgatonegro.database.AppDataBase
import com.example.elgatonegro.database.DetallePedidoDao
import com.example.elgatonegro.model.Book
import com.example.elgatonegro.model.DetallePedido
import com.example.elgatonegro.utils.Constants
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_detalle_libro.*
import kotlinx.android.synthetic.main.fragment_detalle_libro.view.*
import kotlinx.android.synthetic.main.item_book.*
import kotlinx.android.synthetic.main.item_book.view.*
import java.util.concurrent.Executors

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [fragmentDetalleLibro.newInstance] factory method to
 * create an instance of this fragment.
 */
class fragmentDetalleLibro : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var titulo:TextView
    private lateinit var autor:Button
    private lateinit var precio:Button
    private lateinit var genero:Button
    private lateinit var descripcion:TextView
    private lateinit var portada:ImageView
    private lateinit var comprar:Button
    var oldSel:Int = 0
    var oldCant:Int = 0
    lateinit var bookCambiar :Book
    private var selectedBooks= mutableListOf<Book>()

    private val appDataBase : AppDataBase by lazy {
        AppDataBase.getInstancia(requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        parentFragmentManager.setFragmentResultListener("key_item", this) { key, bundle ->
            // We use a String here, but any type that can be put in a Bundle is supported
            val resultBook = bundle.getSerializable(Constants.KEY_BOOK) as Book

            // Do something with the result...
            val tituloD = resultBook.titulo
            val autorD = resultBook.autor
            val generoD = resultBook.genero
            val precioD = resultBook.precio
            val descripcionD = resultBook.descripcion
            val portadaD = resultBook.url
            var idD = resultBook.id

            titulo.setText(tituloD)
            autor.setText(autorD)
            precio.setText("$ "+String.format("%.2f",precioD))
            genero.setText(generoD)
            descripcion.setText(descripcionD)
            Picasso.get().load(portadaD).error(R.drawable.ic_launcher_background).into(portada)
            oldSel = resultBook.seleccionado
            oldCant = resultBook.cantidad

            bookCambiar = Book(idD,tituloD,autorD,resultBook.precio,descripcionD, portadaD,generoD,1, 1)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_detalle_libro, container, false)

        view.btnComprarDetalle.setOnClickListener{
            val mess = "Libro agregado al carrito"

            if(oldSel==0) {
                Executors.newSingleThreadExecutor().execute {
                    appDataBase.bookDao().update(bookCambiar)
                }

                Executors.newSingleThreadExecutor().execute {
                    val detalleP = DetallePedido(
                        0,
                        -1,
                        bookCambiar.id,
                        1,
                        bookCambiar.precio,
                        bookCambiar.precio
                    )
                    appDataBase.detallePedidoDao().insert(detalleP)
                }
            }
            Toast.makeText(context,mess ,Toast.LENGTH_SHORT).show()
        }
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment fragmentDetalleLibro.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            fragmentDetalleLibro().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        titulo = view.findViewById(R.id.txtTituloDetalle)
        autor = view.findViewById(R.id.btnAutorDetalle)
        precio = view.findViewById(R.id.btnPrecioDetalle)
        genero = view.findViewById(R.id.btnGeneroDetalle)
        descripcion = view.findViewById(R.id.txtDescripcionDetalle)
        portada = view.findViewById(R.id.imgPortadaDetalle)
        comprar = view.findViewById(R.id.btnComprarDetalle)

    }

}