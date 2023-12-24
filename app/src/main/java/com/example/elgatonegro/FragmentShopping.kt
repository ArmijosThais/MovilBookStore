package com.example.elgatonegro

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.elgatonegro.database.AppDataBase
import com.example.elgatonegro.model.Book
import com.example.elgatonegro.model.DetallePedido
import com.example.elgatonegro.model.Pedido
import com.example.elgatonegro.model.User
import com.example.elgatonegro.utils.BookAdapter
import com.example.elgatonegro.utils.Constants
import kotlinx.android.synthetic.main.fragment_shopping.*
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Date
import java.util.concurrent.Executors
import kotlin.math.roundToLong

class FragmentShopping : Fragment() {
    lateinit var userTA: User
    private lateinit var recicleViewBooks: RecyclerView
    private lateinit var totalFinalCarrito: TextView
    private lateinit var adapter : BookAdapter
    private var books: List<Book> = listOf()
    private var totalCarrito=0f
    private var subtotales = mutableListOf<Float>()
    private lateinit var contexto : Context
    private val appDataBase : AppDataBase by lazy {
        AppDataBase.getInstancia(requireContext())
    }
    private var contador:Int=0
    lateinit var usuario:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
//            userTA = it.getSerializable("key_usuarioMain") as User
        }

        subtotales.clear()
        var listadoLibrosCarrito = appDataBase.detallePedidoDao().getDetalleByIdPedido(-1)

        if(listadoLibrosCarrito.size >= 1) {
            for (i in 0..listadoLibrosCarrito.size - 1) {
                subtotales.add(listadoLibrosCarrito.get(i).subtotal)
            }
            totalCarrito = subtotales.sum()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.contexto = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val intent = inflater.inflate(R.layout.fragment_shopping, container, false)

        return intent
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentShopping().apply {
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        usuario = view.findViewById(R.id.txtNombreAccount)

        val layoutManager = LinearLayoutManager(context)
        recicleViewBooks = view.findViewById(R.id.rvBooks)
        recicleViewBooks.layoutManager = layoutManager
        recicleViewBooks.setHasFixedSize(true)

        cargarAdaptador(recicleViewBooks)

        txtTotalCarritoShop.setText("$ "+totalCarrito.toString())

        btnComprar.setOnClickListener {
            if(appDataBase.detallePedidoDao().getSumSubtotales()>=1) {
                val dialog = AlertDialog.Builder(contexto)
                    .setTitle("¿Desea continuar?")
                    .setMessage("La compra será oficial")
                    .setNegativeButton("Cancelar") { view, _ ->
                        Toast.makeText(context, "Acción cancelada", Toast.LENGTH_SHORT).show()
                        view.dismiss()
                    }
                    .setPositiveButton("Continuar", DialogInterface.OnClickListener { dialog, id ->
                        val totalCarrito = appDataBase.detallePedidoDao().getSumSubtotales()
                        val dateFormat = SimpleDateFormat("d MMM yyyy")
                        val date = dateFormat.format(Date())
                        val nuevo = Pedido(0, date.toString(), "user", "direccion", totalCarrito)
                        appDataBase.pedidoDao().insert(nuevo)

                        val ultimo_pedido = appDataBase.pedidoDao().getLastPedido().get(0) as Pedido
                        appDataBase.detallePedidoDao().asignarNuevoPedidoDetalles(ultimo_pedido.id)

                        appDataBase.bookDao().cleanSelectedBooks()
                        actualizarSuma()
                        Toast.makeText(context, "Compra Finalizada con Éxito", Toast.LENGTH_SHORT)
                            .show()
                    })
                    .setCancelable(false)
                    .create()
                dialog.show()
            }else{
                Toast.makeText(context, "Carrito de Compras Vacío", Toast.LENGTH_SHORT)
            }
        }
    }

    fun cargarAdaptador(recicleView: RecyclerView){
        adapter = BookAdapter(books)
        recicleView.adapter = adapter

        adapter.setOnClickListenerEliminar = {

            val dialog = AlertDialog.Builder(contexto)
                .setTitle("¿Desea eliminar?")
                .setMessage(it.titulo)
                .setNegativeButton("Cancelar"){ view, _ ->
                    Toast.makeText(context, "Acción cancelada", Toast.LENGTH_SHORT).show()
                    view.dismiss()
                }
                .setPositiveButton("Continuar", DialogInterface.OnClickListener{
                        dialog, id ->
                            val noSelectedBook:Book = Book(it.id,it.titulo,it.autor,it.precio,it.descripcion,it.url,it.genero,0,0)
                            appDataBase.bookDao().update(noSelectedBook)
                            appDataBase.detallePedidoDao().deleteByIdLibro(noSelectedBook.id)
                            actualizarSuma()
                })
                .setCancelable(false)
                .create()
            dialog.show()

        }

        adapter.setOnClickBook = {
            val bundle = Bundle().apply {
                putSerializable(Constants.KEY_BOOK,it)
            }
            parentFragmentManager.setFragmentResult("key_item",bundle)
            val newFragment = fragmentDetalleLibro()
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.nav_host_fragment,newFragment).commit()

        }

        adapter.setOnClickListenerSumar = {
            contador = appDataBase.detallePedidoDao().getCantidadDetalleByIdLibro(it.id).get(0)
            contador+=1
            appDataBase.detallePedidoDao().updateValuesDetalleByIdLibro(it.id,contador)
            appDataBase.bookDao().updateCantidadByIdLibro(it.id,contador)
            actualizarSuma()
        }

        adapter.setOnClickListenerRestar = {
            contador = appDataBase.detallePedidoDao().getCantidadDetalleByIdLibro(it.id).get(0)
            if(contador>1){
                contador-=1
                appDataBase.detallePedidoDao().updateValuesDetalleByIdLibro(it.id,contador)
                appDataBase.bookDao().updateCantidadByIdLibro(it.id,contador)
            }
            actualizarSuma()
        }

        appDataBase.bookDao().getSelectedBooks().observe( viewLifecycleOwner,{
                books -> adapter.updateListBooks(books)
        })
    }

    fun actualizarSuma(){
        val totalCarrito = appDataBase.detallePedidoDao().getSumSubtotales()
        txtTotalCarritoShop.setText("$ "+String.format("%.2f",totalCarrito))
    }
}
