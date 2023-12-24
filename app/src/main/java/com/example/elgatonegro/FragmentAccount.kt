package com.example.elgatonegro

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.SearchRecentSuggestionsProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.elgatonegro.database.AppDataBase
import com.example.elgatonegro.model.Pedido
import com.example.elgatonegro.model.User
import kotlinx.android.synthetic.main.fragment_account.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentAccount.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentAccount : Fragment() {

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var userTA: User
    private val appDataBase : AppDataBase by lazy {
        AppDataBase.getInstancia(requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            userTA = it.getSerializable("key_usuarioMain") as User
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView: View = inflater.inflate(R.layout.fragment_account, container, false)

        return rootView
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentAccount.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentAccount().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        txtNombreAccount.setText(userTA.nombre+" "+userTA.apellido)
        txtCelularAccount.setText(userTA.telefono)
        txtDireccionAccount.setText(userTA.direccion)
        txtEmailAccount.setText(userTA.email)
        txtNacimientoAccount.setText(userTA.fecha_nacimiento)

        btnListaPedidosAccount.setOnClickListener {
            appDataBase.pedidoDao().updateDatosPedido(userTA.cedula, userTA.direccion)
            val pedidos = appDataBase.pedidoDao().getPedidoByUsuario(userTA.cedula) as List<Pedido>
            var arrayPedidos = arrayOfNulls<String>(pedidos.size)

            for (i in 0..pedidos.size - 1) {
                arrayPedidos.set(i,"Fecha: ${pedidos.get(i).fecha_pedido} - Total: $ "+String.format("%.2f",pedidos.get(i).total))
            }

            val dialog = AlertDialog.Builder(context)
            dialog.setTitle("Mis Pedidos")
                .setItems(arrayPedidos,
                    DialogInterface.OnClickListener { dialog, which ->
                        // The 'which' argument contains the index position
                        // of the selected item
                    })
            dialog.create()
            dialog.show()
        }

        btnCerrarSesionAccount.setOnClickListener {
            val intent = Intent(context,LoginActivity::class.java)
            startActivity(intent)
        }
    }
}