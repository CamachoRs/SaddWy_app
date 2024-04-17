package com.example.saddwy

import Config.Config
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject

class Recuperar_Correo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_recuperar_correo)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<Button>(R.id.btn_Correo).setOnClickListener {
            GlobalScope.launch(Dispatchers.Main) {
                try {
                    peticionRecuperar()
                } catch (error:Exception) {
                    println(error)
                }
            }
        }
    }

    suspend fun peticionRecuperar() {
        var queue = Volley.newRequestQueue(this)
        var datos = JSONObject().apply {
            put("correo", findViewById<EditText>(R.id.Text_CorreoRecuperacion).text)
        }
        var request = JsonObjectRequest(
            Request.Method.POST, "${Config().urlBase}recovery/", datos,
            {response ->
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Error")
                builder.setMessage(response.getString("mensaje"))
                builder.setPositiveButton("Aceptar") {dialog, which ->
                    var intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    dialog.dismiss()
                    startActivity(intent)
                }
                val dialog: AlertDialog = builder.create()
                dialog.show()
            },
            {error ->
                var respuesta = JSONObject(String(error.networkResponse.data))
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Error")
                builder.setMessage(respuesta.getString("mensaje"))
                builder.setPositiveButton("Aceptar",null)
                val dialog: AlertDialog = builder.create()
                dialog.show()
            }
        )
        queue.add(request)
    }
}