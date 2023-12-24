package com.example.elgatonegro

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.elgatonegro.database.AppDataBase
import com.example.elgatonegro.model.Book
import com.example.elgatonegro.utils.BookAdapter
import com.example.elgatonegro.utils.BookAdapterInicio
import com.example.elgatonegro.utils.Constants
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentHome.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentHome : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var recycleViewAllBooks: RecyclerView
    private lateinit var adapter : BookAdapterInicio
    private var books: List<Book> = listOf()
    private val appDataBase : AppDataBase by lazy {
        AppDataBase.getInstancia(requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val intent =  inflater.inflate(R.layout.fragment_home, container, false)
        return intent
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentHome.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentHome().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = BookAdapterInicio(books)

        recycleViewAllBooks = view.findViewById(R.id.rvLibrosInicio)
        recycleViewAllBooks.setHasFixedSize(true)
        recycleViewAllBooks.adapter = adapter

        adapter.setOnClickBook = {
            val bundle = Bundle().apply {
                putSerializable(Constants.KEY_BOOK,it)
            }
            parentFragmentManager.setFragmentResult("key_item",bundle)
            val newFragment = fragmentDetalleLibro()
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.nav_host_fragment,newFragment).commit()
            chipgGeneros.clearCheck()
        }

        appDataBase.bookDao().getBooks().observe( viewLifecycleOwner,{
                books ->adapter.updateListBooks(books)
        })

        chipgGeneros.setOnCheckedStateChangeListener { group, checkedIds ->
            if (checkedIds.isEmpty()){
                appDataBase.bookDao().getBooks().observe(viewLifecycleOwner, { books ->
                    adapter.updateListBooks(books)
                })
            }else{
                val chip = group.findViewById<Chip>(checkedIds[0])

                if ( chip != null){
                    when(chip.text.toString()){
                        "Fantasía y Ciencia Ficción" -> {
                            appDataBase.bookDao().getBooksByGender("Fantasía").observe( viewLifecycleOwner,{
                                    books ->adapter.updateListBooks(books)
                            })
                        }
                        "Romance" -> {
                            appDataBase.bookDao().getBooksByGender("Romance").observe( viewLifecycleOwner,{
                                    books ->adapter.updateListBooks(books)
                            })
                        }
                        "Misterio" -> {
                            appDataBase.bookDao().getBooksByGender("Misterio").observe( viewLifecycleOwner,{
                                    books ->adapter.updateListBooks(books)
                            })
                        }
                        "Terror y Suspenso" -> {
                            appDataBase.bookDao().getBooksByGender("Terror").observe( viewLifecycleOwner,{
                                    books ->adapter.updateListBooks(books)
                            })
                        }
                        "Ciencia" -> {
                            appDataBase.bookDao().getBooksByGender("Ciencia").observe( viewLifecycleOwner,{
                                    books ->adapter.updateListBooks(books)
                            })
                        }
                    }
                }
            }
        }

        edtBuscador.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(filterText: CharSequence?, start: Int, before: Int, count: Int) {
                if (filterText?.length!! > 0)
                {
                    chipgGeneros.clearCheck()
                    appDataBase.bookDao().getBooksByTitle(filterText.toString().uppercase()).observe(viewLifecycleOwner, { books ->
                        adapter.updateListBooks(books)
                    })
                }
                else
                {
                    appDataBase.bookDao().getBooks().observe(viewLifecycleOwner, { books ->
                        adapter.updateListBooks(books)
                    })
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }
}