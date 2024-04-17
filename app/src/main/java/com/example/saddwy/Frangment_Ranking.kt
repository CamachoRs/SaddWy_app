package com.example.saddwy

import Adapter.AdapterRanking
import Config.Config
import android.animation.AnimatorInflater
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Frangment_Ranking.newInstance] factory method to
 * create an instance of this fragment.
 */
class Frangment_Ranking : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var puesto: TextView
    private lateinit var imgPerfil: CircleImageView
    private lateinit var nombre: TextView
    private lateinit var puntos: TextView
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    lateinit var recycler: RecyclerView
    lateinit var Token:String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_frangment__ranking, container, false)

        puesto = view.findViewById(R.id.Puntaje_Usuario)
        imgPerfil = view.findViewById(R.id.ImagenRanking)
        nombre = view.findViewById(R.id.Nombre_Usuario)
        puntos = view.findViewById(R.id.Puntaje_Usuario)
        recycler = view.findViewById(R.id.Recycler_Ranking)


        prefs = requireActivity().
        getSharedPreferences("sesion", Context.MODE_PRIVATE)
        //Obtener el token guardado
        Token =  prefs.getString("token","").toString()

        GlobalScope.launch(Dispatchers.Main) {
            try {
                peticionRanking()
            } catch (error:Exception) {
                println(error)
            }
        }
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Frangment_Ranking.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Frangment_Ranking().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    suspend fun peticionRanking() {
        val queue = Volley.newRequestQueue(activity)
        val request = object: JsonObjectRequest(
            Method.GET, "${Config().urlBase}ranking/", null,
            { response ->
                var dato = response.getJSONObject("dato")
                var listado = dato.getJSONObject("listado")
                var usuario = dato.getJSONObject("usuario")
                puesto.text = usuario.getString("puesto")
                nombre.text = usuario.getString("nombre")
                puntos.text = usuario.getString("puntos")
                Glide.with(this)
                    .load(Config.foto)
                    .into(imgPerfil)

                cargarLista(listado)
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

    fun cargarLista(listaRanking: JSONObject) {
        recycler.layoutManager = LinearLayoutManager(activity)
        recycler.adapter = AdapterRanking(activity, listaRanking)
    }
}