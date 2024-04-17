package com.example.saddwy

import Adapter.AdapterHome
import Config.Config
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class Fragmen_Inicio : Fragment() {
    // TODO: Rename and change types of parameters
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var Token:String
    lateinit var recycler: RecyclerView
    //traerinformacion
    private lateinit var prefs: SharedPreferences

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
        var view = inflater.inflate(R.layout.fragment_fragmen__inicio, container, false)
        Buscarstart()
        //El recycler view para progreso
        recycler = view.findViewById(R.id.Recycler_Card)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefs = requireActivity().
        getSharedPreferences("sesion", Context.MODE_PRIVATE)
        Token =  prefs.getString("token","").toString()
    }


    fun Buscarstart(){
        GlobalScope.launch {
            try {
                start()
            }catch (error: Exception){

            }
        }
    }

    suspend fun start() {
        val url = Config().urlBase + "start/"
        val queue = Volley.newRequestQueue(activity)

        val stringRequest = object : JsonObjectRequest(
            Method.GET,
            url,
            null,
            { response ->
                // Manejar la respuesta del servidor aquí
                val listaProgreso = response.getJSONArray("dato")
                // El progreso del usuario
                cargarLista(listaProgreso)
            },
            { error ->
                Toast.makeText(activity, "Error en el servidor {$error}", Toast.LENGTH_LONG).show()
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

    fun cargarLista(listaProgreso: JSONArray) {
        recycler.layoutManager = LinearLayoutManager(activity)
        recycler.adapter = AdapterHome(requireActivity(), listaProgreso)
    }



}


