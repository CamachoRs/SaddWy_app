package com.example.saddwy

import Config.Config
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.saddwy.Principal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import org.json.JSONObject
import org.w3c.dom.Text
import java.util.Objects



class MainActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences("sesion", Context.MODE_PRIVATE)

        var Inicio:Button=findViewById(R.id.btn_Inicio)

        if (verificarSesion()) {

            startActivity(Intent(this, Principal::class.java))
            finish()
        }

        // aqui se muestra el password

        MostrarContrasena()

        Inicio.setOnClickListener{
            GlobalScope.launch(Dispatchers.Main) {
                try {
                    login()
                } catch (error: Exception) {
                    Toast.makeText(this@MainActivity,
                        "Error en la peticion" + "{$error}", Toast.LENGTH_LONG
                    ).show()
                }
            }


        }
    }


    suspend fun login(){
        val usuario: TextView = findViewById(R.id.TextUsuario)
        val password: TextView = findViewById(R.id.TextPassword)
        val url = Config().urlBase + "login/"
        val queue = Volley.newRequestQueue(this)
        val parametros = JSONObject()
        parametros.put("correo", usuario.text)
        parametros.put("password", password.text)

        val stringRequest =  JsonObjectRequest(
            Request.Method.POST,
            url,
            parametros,
            { responce ->

                session(usuario.toString())

                val Principal = Intent(this, Principal::class.java)
                startActivity(Principal)
            },
            { error ->
                var respuesta = JSONObject(String(error.networkResponse.data))
                // validar= respuesta.getBoolean("validar")

                val builder = AlertDialog.Builder(this)
                builder.setTitle("Error")
                builder.setMessage(respuesta.getString("mensaje"))
                builder.setPositiveButton("Aceptar",null)
                val dialog: AlertDialog = builder.create()
                dialog.show()
            }

        )
        queue.add(stringRequest)
    }

    private fun session(usuario: String){
        val prefs = getSharedPreferences("sesion", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString("usuario", usuario)
        editor.apply()
    }

    private fun verificarSesion(): Boolean {
        val prefs = getSharedPreferences("sesion", Context.MODE_PRIVATE)
        return prefs.contains("usuario")
    }

    private fun MostrarContrasena(){
        val checkBoxMostrarContrasena: CheckBox = findViewById(R.id.checkBoxMostrarContrasena)
        val password: EditText = findViewById(R.id.TextPassword)

        checkBoxMostrarContrasena.setOnCheckedChangeListener() {buttonView, isChecked ->
            if (isChecked) {
                password.inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                password.inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
        }
    }


}


