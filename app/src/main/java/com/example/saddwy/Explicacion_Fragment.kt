package com.example.saddwy

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import org.json.JSONObject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

class Explicacion_Fragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        var view= inflater.inflate(R.layout.fragment_explicacion_, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Obtén una referencia al ProgressBar
        val progressBar = view.findViewById<ProgressBar>(R.id.progress_Explicacion)
        val explanation = arguments?.getString("explanation")
        val textExplicacion = view.findViewById<TextView>(R.id.text_explicacion)

        val json = explanation

        try {
            val obj = JSONObject(json)
            textExplicacion.text = obj.getString("explanation")
            Log.d("My App", obj.toString())
        } catch (t: Throwable) {
            Log.e("My App", "Could not parse malformed JSON: \"$json\"", t)
        }

        // Establece el valor de progreso
        progressBar.progress = 10 // Por ejemplo, establece el progreso en 50
    }
}