package com.example.saddwy

import Adapter.AdapterProgeso
import Config.Config
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Fragment_Perfil.newInstance] factory method to
 * create an instance of this fragment.
 */
class Fragment_Perfil : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)

        }

    }
    lateinit var  TextUsuario: TextView
    lateinit var  TextFecha: TextView
    lateinit var  TextCorreo: TextView
    lateinit var  imgPerfil: CircleImageView
    lateinit var Token:String
    lateinit var recycler: RecyclerView
    //los dias de la semana para racha
    lateinit var Lunes:CardView
    lateinit var Martes:CardView
    lateinit var Miercoles:CardView
    lateinit var Jueves:CardView
    lateinit var Viernes:CardView
    lateinit var Sabado:CardView
    lateinit var Domingo:CardView

    lateinit var btnActualizar:ImageButton
    //Style para la racha
    private val fondoActivo = R.drawable.circulo_activo
    private val fondoInactivo = R.drawable.circulo_inactivo
    //traerinformacion
    private lateinit var prefs: SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment__perfil, container, false)
        BuscarPerfil()

        TextUsuario = view.findViewById(R.id.TextUsuario)
        TextFecha = view.findViewById(R.id.TextFecha)
        TextCorreo = view.findViewById(R.id.TextoCorreo)
        imgPerfil = view.findViewById(R.id.id_Imagen)

        //Cambiar a editor editar
        /*var btn_Editar:Button = view.findViewById<Button>(R.id.btn_Editar)

            btn_Editar.setOnClickListener{
                val intent =    Intent(activity,EditarPerfil::class.java)
                startActivity(intent)
            }*/
        //Inicilizo los dias de racha
        Lunes = view.findViewById(R.id.lunes)
        Martes = view.findViewById(R.id.Martes)
        Miercoles=view.findViewById(R.id.Miercoles)
        Jueves= view.findViewById(R.id.Jueves)
        Viernes= view.findViewById(R.id.Viernes)
        Sabado = view.findViewById(R.id.Sabado)
        Domingo = view.findViewById(R.id.Domingo)
        //El recycler view para progreso
        btnActualizar= view.findViewById<ImageButton>(R.id.btn_Actualizar)
        recycler = view.findViewById(R.id.Progreso)

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Fragment_Perfil.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Fragment_Perfil().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefs = requireActivity().getSharedPreferences("sesion", Context.MODE_PRIVATE)
        Config.prefs = prefs
        Token =  prefs.getString("token","").toString()

        //ir a a la vista actualizar
        view.findViewById<ImageButton>(R.id.btn_Actualizar).setOnClickListener {
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            var fragment = editar_usuario()
            val args = Bundle()
            args.putString("nombre", TextUsuario.text.toString())
            args.putString("correo", TextCorreo.text.toString())
            fragment.arguments = args
            transaction.replace(R.id.fragment_layout_main, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
        //Obtener el token guardado
        Token = prefs.getString("token", "").toString()
    }

            fun BuscarPerfil() {
                GlobalScope.launch {
                    try {
                        Perfil()
                    } catch (error: Exception) {

                    }
                }
            }



    suspend fun Perfil() {
        val url = Config().urlBase + "profile/"
        val queue = Volley.newRequestQueue(activity)
        // Se preparan los parámetros

        val stringRequest = object : JsonObjectRequest(
            Method.GET,
            url,
            null,
            { response ->
                // Manejar la respuesta del servidor aquí
                    var dato = response.getJSONObject("dato")
                    var usuario = dato.getJSONObject("usuario")
                    var racha = usuario.getJSONObject("racha")
                // lleno la vista con la informacio del appi
                    TextUsuario.text = usuario.getString("nombre")
                    TextCorreo.text = usuario.getString("correo")
                    TextFecha.text = usuario.getString("registro")
                    val foto = usuario.getString("foto")

                Config.foto = foto
                // Racha del usuario
                Lunes.setBackgroundResource(if (racha.getBoolean("Lunes")) fondoActivo else fondoInactivo)
                Martes.setBackgroundResource(if (racha.getBoolean("Martes")) fondoActivo else fondoInactivo)
                Miercoles.setBackgroundResource(if (racha.getBoolean("Miercoles"))fondoActivo else fondoInactivo)
                Jueves.setBackgroundResource(if (racha.getBoolean("Jueves"))fondoActivo else fondoInactivo)
                Viernes.setBackgroundResource(if (racha.getBoolean("Viernes"))fondoActivo else fondoInactivo)
                Sabado.setBackgroundResource(if (racha.getBoolean("Sábado"))fondoActivo else fondoInactivo)
                Domingo.setBackgroundResource(if (racha.getBoolean("Domingo"))fondoActivo else fondoInactivo)
                Glide.with(this)
                    .load(foto)
                    .into(imgPerfil)

                //El progeso del usuario
                cargarLista(dato.getJSONArray("progreso"))

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

        fun cargarLista(listaProgreso:JSONArray){
            recycler.layoutManager = LinearLayoutManager(activity)
            recycler.adapter = AdapterProgeso(activity,listaProgreso)
        }

    private fun startTokenRefreshTimer() {
        // Llamar al método para iniciar el temporizador de actualización de token
        context?.let { Config().startTokenRefreshTimer(it) }
    }



}