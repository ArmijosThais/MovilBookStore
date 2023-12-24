package com.example.elgatonegro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.elgatonegro.database.AppDataBase
import com.example.elgatonegro.databinding.ActivityMainBinding
import com.example.elgatonegro.databinding.FragmentShoppingBinding
import com.example.elgatonegro.model.Book
import com.example.elgatonegro.model.User
import com.example.elgatonegro.utils.BookAdapter
import com.example.elgatonegro.utils.Constants
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    lateinit var usuarioIngresado:User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = intent.extras
        bundle?.let {
            usuarioIngresado = bundle.getSerializable("key_usuario") as User
        }

        val bundleUS = Bundle().apply {
            putSerializable("key_usuarioMain", usuarioIngresado)
        }

        val bottomNavigationView:BottomNavigationView = binding.bottomNavigationView
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        val navController = navHostFragment.navController
        navController.setGraph(R.navigation.nav_graph,bundleUS)

        bottomNavigationView.setupWithNavController(navController)


    }

}