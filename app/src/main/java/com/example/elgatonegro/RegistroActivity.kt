package com.example.elgatonegro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.elgatonegro.database.AppDataBase
import com.example.elgatonegro.databinding.ActivityRegistroBinding
import com.example.elgatonegro.model.User
import java.util.concurrent.Executors

class RegistroActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistroBinding
    private val appDataBase : AppDataBase by lazy {
        AppDataBase.getInstancia(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        eventos()
    }

    fun eventos(){
        binding.edtConfirmContraseARegistro.setOnFocusChangeListener { v, hasFocus ->  verificacionContraseñas()}
        binding.edtEmailRegistro.setOnFocusChangeListener { v, hasFocus -> validarCorreo() }
        binding.edtFechaNacimientoRegistro.setOnClickListener {
            showDatePickerDialog()
        }
    }

    private fun showDatePickerDialog(){
        val datePicker = DatePickerFragment{day, month, year -> onDateSelected(day, month, year)}
        datePicker.show(supportFragmentManager, "datepicker")
    }

    fun onDateSelected(day:Int, month:Int, year:Int){
        val realMonth = month+1
        binding.edtFechaNacimientoRegistro.setText("$day/$realMonth/$year")
    }

    fun crearUsuario(user: User){
        Executors.newSingleThreadExecutor().execute {
            appDataBase.userDao().insert(user)
            runOnUiThread {
                Toast.makeText(this,"Usuario Registrado con Éxito", Toast.LENGTH_LONG).show()
                onBackPressed()
            }
        }
    }

    fun verificacionContraseñas(){
        if (binding.edtConfirmContraseARegistro.text.toString().equals(binding.edtContraseARegistro.text.toString())){
            binding.txtAviso.setText("")
            binding.btnRegistrarseRegistro.setOnClickListener {
                registrarContinuar()
            }
        }else{
            binding.txtAviso.setText("Las contraseñas deben coinsidir")
            binding.btnRegistrarseRegistro.setOnClickListener {
                Toast.makeText(this,"Campos no váldos", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun validarCorreo(){
        val correo = binding.edtEmailRegistro.text.toString()
        val usuarios = appDataBase.userDao().getUsers() as List<User>
        var duplicado = false

        for (i in 0..usuarios.size-1){
            if(usuarios.get(i).email.equals(correo)){
                duplicado=true
            }
        }

        if(duplicado){
            binding.txtAlertaCorreo.setText("Ya existe un usuario registrado bajo este correo")
            binding.btnRegistrarseRegistro.setOnClickListener {
                Toast.makeText(this,"Campos no váldos", Toast.LENGTH_LONG).show()
            }
        }else{
            binding.txtAlertaCorreo.setText("")
            binding.btnRegistrarseRegistro.setOnClickListener {
                registrarContinuar()
            }
        }
    }

    fun registrarContinuar(){
        val cedula = binding.edtCedulaRegistro.text.toString()
        val nombre = binding.edtNombreRegistro.text.toString()
        val apellido = binding.edtApellidoRegistro.text.toString()
        val telefono = binding.edtTelefonoRegistro.text.toString()
        val direccion = binding.edtDireccionRegistro.text.toString()+" - "+binding.edtDireccionRegistro2.text.toString()+" - "+binding.edtDireccionRegistro3.text.toString()
        val fecha_nacimiento = binding.edtFechaNacimientoRegistro.text.toString()
        val email = binding.edtEmailRegistro.text.toString()
        val contraseña = binding.edtContraseARegistro.text.toString()

        val usuarioNuevo = User(cedula,nombre,apellido,telefono,email,direccion,fecha_nacimiento,contraseña)

        crearUsuario(usuarioNuevo)
        val bundle = Bundle().apply {
            putSerializable("key_usuario", usuarioNuevo)
        }
        val intent = Intent(this,MainActivity::class.java).apply {
            putExtras(bundle)
        }
        startActivity(intent)
    }
}