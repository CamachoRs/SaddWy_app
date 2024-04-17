package com.example.saddwy

import Config.Config
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.viewpager.widget.ViewPager
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Cuestionario : AppCompatActivity() {
    lateinit var Token:String
    lateinit var Titulo:TextView
    private var id_usuario:Int = 0
    private var id_pregunta:Int = 0
    var Intento:Int = 0
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cuestionario)


        Titulo =findViewById(R.id.titulo_card)


        // Recuperar el nivel_id del Intent
        id_usuario  = intent.getIntExtra("nivel_id", -1)
        id_pregunta = intent.getIntExtra("id_pregunta",1)
        Intento = intent.getIntExtra("Intento",0)

        prefs = this.
        getSharedPreferences("sesion", Context.MODE_PRIVATE)
        Token =  prefs.getString("token","").toString()
        //llamo a la funcion Buscar

        GlobalScope.launch {
                try {
                    peticionCuestionario()
                }catch (error: Exception){

                }
            }
    }



    fun BuscarCuestionario(){

    }
    suspend fun peticionCuestionario() {
        val queue = Volley.newRequestQueue(this)
        val request = object: JsonObjectRequest(
            Method.GET, "${Config().urlBase}questions/${id_usuario}", null,
            { response ->
                var dato = response.getJSONArray("dato")
                val explanation = dato.getString(id_pregunta)
                val args = Bundle()

                args.putString("explanation", explanation)
                args.putInt("id_pregunta",id_pregunta)
                args.putInt("nivel_id",id_usuario)
                args.putInt("Intento",Intento)


                // Crear instancias de tus fragmentos y pasarles los argumentos
                var fragmentoExplicacion = Explicacion_Fragment()
                fragmentoExplicacion.arguments = args

                val fragmentoPregunta = Pregunta_Fragment()
                fragmentoPregunta.arguments = args

                var viewPager: ViewPager = findViewById(R.id.viewPager) as ViewPager
                var tableyout: TabLayout = findViewById(R.id.tablayout) as TabLayout

                val fragmentAdapter = FragmentAdapter(supportFragmentManager)
                fragmentAdapter.addFragment(fragmentoExplicacion, "Explicacion")
                fragmentAdapter.addFragment(fragmentoPregunta, "Pregunta")

                viewPager.adapter = fragmentAdapter
                tableyout.setupWithViewPager(viewPager)
            },
            { error ->
                println(error)
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Token ${Token}"
                return headers
            }
        }
        queue.add(request)
    }
}