package com.example.elgatonegro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.elgatonegro.database.AppDataBase
import com.example.elgatonegro.databinding.ActivityLoginBinding
import com.example.elgatonegro.databinding.ActivityMainBinding
import com.example.elgatonegro.model.User
import com.squareup.picasso.Picasso

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val appDataBase : AppDataBase by lazy {
        AppDataBase.getInstancia(this)
    }
    lateinit var usuario: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val url = "https://thumbs.dreamstime.com/b/mirada-principal-linda-negra-de-la-cara-del-gatito-gato-que-se-sienta-en-ara%C3%B1a-ejecuci%C3%B3n-car%C3%A1cter-divertido-historieta-animal-113621764.jpg"
//        val url = "https://st4.depositphotos.com/3573725/29718/v/600/depositphotos_297182836-stock-illustration-black-cat-bookshop-logo.jpg"
//        Picasso.get().load(url).error(R.drawable.ic_launcher_background).into(binding.imgUsurario)

        eventos()
    }

    fun eventos(){
        binding.btnregistrarseLogin.setOnClickListener {
            startActivity(Intent(this, RegistroActivity::class.java))
        }

        binding.btnIngresarLogin.setOnClickListener {
            validarUsuario()
        }
    }

    fun validarUsuario(){
        val correo = binding.edtUsuarioLogin.text.toString()
        val contraseña = binding.edtPasswordLogin.text.toString()
        val usuarios = appDataBase.userDao().getUsers() as List<User>
        var usuarioValido = false

        for (i in 0..usuarios.size-1){
            if(usuarios.get(i).email.equals(correo) && usuarios.get(i).clave.equals(contraseña)){
                usuarioValido=true
                usuario = usuarios.get(i)
            }
        }

        if(usuarioValido){
            val email = binding.edtUsuarioLogin.text.toString()

            val bundle = Bundle().apply {
                putSerializable("key_usuario", usuario)
            }
            val intent = Intent(this,MainActivity::class.java).apply {
                putExtras(bundle)
            }
            startActivity(intent)
        }else{
            Toast.makeText(this,"Usuario y/o Contraseña incorrectos", Toast.LENGTH_LONG).show()
        }
    }
}