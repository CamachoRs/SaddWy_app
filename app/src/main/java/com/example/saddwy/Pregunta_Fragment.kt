package com.example.saddwy

import Config.Config
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


class Pregunta_Fragment : Fragment() {
    lateinit var Token:String
    private lateinit var prefs: SharedPreferences
    lateinit var Pregunta:TextView
    lateinit var btn_pregunta1:Button
    lateinit var btn_pregunta2:Button
    lateinit var btn_pregunta3:Button
    lateinit var Continuar:Button
    var id_mivel:Int = 0
    var Nintentos:Int = 0
    var id_preg:Int = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
         val view=  inflater.inflate(R.layout.fragment_pregunta_, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Pregunta = view.findViewById(R.id.text_Pregunta)
        btn_pregunta1 = view.findViewById(R.id.btn_pregunta1)
        btn_pregunta2 = view.findViewById(R.id.btn_pregunta2)
        btn_pregunta3 = view.findViewById(R.id.btn_pregunta3)
        Continuar = view.findViewById(R.id.btn_continua)

        Continuar.isEnabled = false

        //llamar al token
        prefs = requireActivity().
        getSharedPreferences("sesion", Context.MODE_PRIVATE)
        Token =  prefs.getString("token","").toString()



        try {

            val explanation = arguments?.getString("explanation").toString()
            //trae id_pregunta
            val id_nivel:Int = arguments?.getInt("nivel_id",0)?:0
            id_mivel = id_nivel

            val id:Int = arguments?.getInt("id_pregunta",0)?:0
             id_preg = id+1
            //traer id Intento
            val Intento:Int = arguments?.getInt("Intento",0)?:0
             Nintentos = Intento
            val json = explanation
            //
            val obj = JSONObject(json)
            val texto_button = obj.getJSONObject("respuesta")
            Pregunta.text = obj.getString("pregunta")
            btn_pregunta1.text = texto_button.getString("1")
            btn_pregunta1.setOnClickListener {
                if (btn_pregunta1.text == texto_button.getString("respuesta")){
                    btn_pregunta1.setBackgroundColor(Color.parseColor("#4CAF50"))
                    Continuar.isEnabled = true

                    Continuar.setOnClickListener {

                        if (id_preg==5){

                            GlobalScope.launch {
                                try {
                                    Cuestion()
                                }catch (error: Exception){

                                }
                            }

                            val builder = AlertDialog.Builder(requireContext())
                            builder.setTitle("Error")
                            builder.setMessage("Felicitaciones por terminar el nivel")
                            builder.setPositiveButton("Aceptar"){dialog,which ->
                                val intel = Intent(activity,Principal::class.java)
                                startActivity(intel)
                                activity?.finish()
                            }
                            val dialog: AlertDialog = builder.create()
                            dialog.show()


                        }else{
                            val  intel= Intent(activity,Cuestionario::class.java)
                            intel.putExtra("id_pregunta", id_preg)
                            intel.putExtra("nivel_id",id_nivel)
                            intel.putExtra("Intento",Nintentos)
                            startActivity(intel)
                            activity?.finish()
                        }

                    }
                }else{
                    btn_pregunta1.setBackgroundColor(Color.parseColor("#FF0000"))
                    Nintentos+1
                }
            }
            btn_pregunta2.text = texto_button.getString("2")
            btn_pregunta2.setOnClickListener {
                if (btn_pregunta2.text == texto_button.getString("respuesta")){
                    btn_pregunta2.setBackgroundColor(Color.parseColor("#4CAF50"))
                    Continuar.isEnabled = true
                }else{
                    btn_pregunta2.setBackgroundColor(Color.parseColor("#FF0000"))
                    Nintentos+1
                }
            }
            btn_pregunta3.text = texto_button.getString("3")
            btn_pregunta3.setOnClickListener{

                if (btn_pregunta3.text == texto_button.getString("respuesta")){
                    btn_pregunta3.setBackgroundColor(Color.parseColor("#FF0000"))
                    Continuar.isEnabled = true
                }else{
                    btn_pregunta3.setBackgroundColor(Color.parseColor("#FF0000"))
                    Nintentos+1
                }




            }
            Log.d("My App", obj.toString())
        } catch (t: Throwable) {
            Log.e("My App", "Could not parse malformed JSON: ")
        }

        val progressBar = view.findViewById<ProgressBar>(R.id.progress_Explicacion)
        // Establece el valor de progreso
        var max = 100
        progressBar.max = max
        progressBar.progress = 5
    }

    suspend fun Cuestion() {
        val url = Config().urlBase + "questions/${id_mivel}/"
        val queue = Volley.newRequestQueue(activity)
        val parametros = JSONObject()
        parametros.put("intentos", Nintentos)
        val stringRequest = object : JsonObjectRequest(
            Method.POST,
            url,
            null,
            { response ->

            },
            { error ->
                Toast.makeText(activity,"Error en el servidor {$error}",
                    Toast.LENGTH_LONG).show()

            }
        ) {
            // Se agregan los encabezados de la solicitud, incluyendo el token JWT si está disponible
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                // Agregar el token JWT si está disponible
                if (Token.isNotEmpty()) {
                    headers["Authorization"] = "Token ${Token}"
                }
                return headers
            }
        }

        queue.add(stringRequest)
    }
}