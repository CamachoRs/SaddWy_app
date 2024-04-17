package com.example.saddwy

import android.content.Intent
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
import org.json.JSONObject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


class Pregunta_Fragment : Fragment() {

    lateinit var Pregunta:TextView
    lateinit var btn_pregunta1:Button
    lateinit var btn_pregunta2:Button
    lateinit var btn_pregunta3:Button
    lateinit var Continuar:Button
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

        val explanation = arguments?.getString("explanation").toString()
        val id:Int = arguments?.getInt("id_pregunta",0)?:0
        val sumar = id+1
        val json = explanation

        try {
            val obj = JSONObject(json)
            val texto_button = obj.getJSONObject("respuesta")
            Pregunta.text = obj.getString("pregunta")
            btn_pregunta1.text = texto_button.getString("1")
            btn_pregunta1.setOnClickListener {
                if (btn_pregunta1.text == texto_button.getString("respuesta")){
                    btn_pregunta1.setBackgroundColor(Color.parseColor("#4CAF50"))
                    Continuar.isEnabled = true

                    Continuar.setOnClickListener {
                        val  intel= Intent(activity,Cuestionario::class.java)
                       intel.putExtra("id_pregunta", sumar)
                        startActivity(intel)
                        activity?.finish()
                    }
                }else{
                    btn_pregunta1.setBackgroundColor(Color.parseColor("#FF0000"))
                }
            }
            btn_pregunta2.text = texto_button.getString("2")
            btn_pregunta2.setOnClickListener {
                if (btn_pregunta2.text == texto_button.getString("respuesta")){
                    btn_pregunta2.setBackgroundColor(Color.parseColor("#4CAF50"))
                    Continuar.isEnabled = true
                }else{
                    btn_pregunta2.setBackgroundColor(Color.parseColor("#FF0000"))
                }
            }
            btn_pregunta3.text = texto_button.getString("3")
            btn_pregunta3.setOnClickListener{

                if (btn_pregunta3.text == texto_button.getString("respuesta")){
                    btn_pregunta3.setBackgroundColor(Color.parseColor("#FF0000"))
                    Continuar.isEnabled = true
                }else{
                    btn_pregunta3.setBackgroundColor(Color.parseColor("#FF0000"))
                }




            }
            Log.d("My App", obj.toString())
        } catch (t: Throwable) {
            Log.e("My App", "Could not parse malformed JSON: \"$json\"", t)
        }

        val progressBar = view.findViewById<ProgressBar>(R.id.progress_Explicacion)
        // Establece el valor de progreso
        progressBar.progress = 10 // Por ejemplo, establece el progreso en 50
    }
}