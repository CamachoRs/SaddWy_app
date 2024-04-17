package com.example.saddwy

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.database.Cursor
import android.media.audiofx.DynamicsProcessing.Config
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.Response
import java.io.File
import java.io.IOException
import kotlin.math.max

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [editar_usuario.newInstance] factory method to
 * create an instance of this fragment.
 */
class editar_usuario : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1:String? = null
    private var param2:String? = null
    private lateinit var foto: ImageView
    private lateinit var urlFoto:String
    private var uriFoto: Uri? = null
    private lateinit var nombre:String
    private lateinit var correo:String
    private lateinit var Token:String
    private lateinit var prefs: SharedPreferences
    private var PICK_IMAGE = 1;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            urlFoto = it.getString("foto").toString()
            nombre = it.getString("nombre").toString()
            correo = it.getString("correo").toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_editar_usuario, container, false)
        //traer token
        prefs = requireActivity().
        getSharedPreferences("sesion", Context.MODE_PRIVATE)
        Token =  prefs.getString("token","").toString()

        //--------
        foto = view.findViewById(R.id.imgUsuarioActualizar)

        foto.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, PICK_IMAGE)
        }

        view.findViewById<EditText>(R.id.txtNombreActualizar).setText(nombre)
        Glide.with(this)
            .load(urlFoto)
            .into(foto)

        view.findViewById<Button>(R.id.btnActualizarActualizar).setOnClickListener {
            GlobalScope.launch(Dispatchers.Main) {
                try {
                    peticionActualizar(
                        requireContext(),
                        view.findViewById<EditText>(R.id.txtNombreActualizar).text.toString(),
                        view.findViewById<EditText>(R.id.txtPasswordActualizar).text.toString(),
                        view.findViewById<EditText>(R.id.txtConfirmarPasswordActualizar).text.toString()
                    )
                } catch (error: Exception) {
                    println(error)
                }
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
         * @return A new instance of fragment ActualizarCuenta.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            editar_usuario().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    suspend fun peticionActualizar(context: Context, nombre:String, password:String, confirmarPassword:String) {
        var validarNombre = ""
        var validarPassword = ""

        val cliente = OkHttpClient()
        val datos = MultipartBody.Builder().setType(MultipartBody.FORM)
        if (uriFoto != null) {
            var file = File(getRealPathFromURI(uriFoto!!))
            datos.addFormDataPart("foto", file.name, file.asRequestBody("image/*".toMediaTypeOrNull()))
        }

        if (nombre.isNotEmpty()) {
            validarNombre = validarNombre(nombre)
            if (validarNombre.length == 0) {
                datos.addFormDataPart("nombre", nombre)
            } else {
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Error")
                builder.setPositiveButton("Aceptar",null)
                builder.setMessage(validarNombre)
                val dialog: AlertDialog = builder.create()
                dialog.show()
            }
        }

        if (password.isNotEmpty()) {
            validarPassword = validarPassword(password, confirmarPassword)
            if (validarPassword.length == 0) {
                datos.addFormDataPart("password", password)
            } else {
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Error")
                builder.setPositiveButton("Aceptar",null)
                builder.setMessage(validarPassword)
                val dialog: AlertDialog = builder.create()
                dialog.show()
            }
        }

        val requestBody = datos.build()
        val request = Request.Builder()
            .url("http://192.168.147.187:8000/api/v01/edit/")
            .addHeader("Authorization", "Token ${Token}")
            .put(requestBody)
            .build()

        cliente.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                println(e)
            }

            override fun onResponse(call: okhttp3.Call, response: Response) {
                if (response.code == 200) {
                    val builder = AlertDialog.Builder(requireContext())
                    builder.setTitle("¡Actualización completa!")
                    builder.setPositiveButton("Aceptar",null)
                    builder.setMessage("¡Información actualizada exitosamente!")
                    val dialog: AlertDialog = builder.create()
                    dialog.show()
                }
            }
        })
    }

    override fun onActivityResult(requestCode:Int, resultCode:Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE) {
            val imageUri = data?.data
            imageUri?.let {
                if (isValidImageFile(requireContext(), it)) {
                    uriFoto = it
                    Glide.with(this)
                        .load(uriFoto)
                        .placeholder(R.drawable.icono_saddwy)
                        .error(R.drawable.icono_saddwy)
                        .into(foto)
                } else {
                    val builder = AlertDialog.Builder(requireContext())
                    builder.setTitle("Error")
                    builder.setMessage("Por favor, asegúrate de cargar una imagen en formato JPG o PNG para completar el proceso")
                    builder.setPositiveButton("Aceptar",null)
                    val dialog: AlertDialog = builder.create()
                    dialog.show()
                    return
                }
            }
        }
    }

    private fun getRealPathFromURI(uri: Uri):String {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor? = context?.contentResolver?.query(uri, projection, null, null, null)
        return cursor?.use {
            val columnIndex: Int = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            it.moveToFirst()
            it.getString(columnIndex)
        } ?: ""
    }

    fun isValidImageFile(context: Context, uri: Uri): Boolean {
        val mimeType = context.contentResolver.getType(uri)
        return mimeType?.startsWith("image/") ?: false && isSupportedImageFormat(mimeType)
    }

    fun isSupportedImageFormat(mimeType: String?): Boolean {
        return mimeType != null
                && (mimeType.equals("image/jpeg", ignoreCase = true)
                || mimeType.equals("image/jpg", ignoreCase = true)
                || mimeType.equals("image/png", ignoreCase = true))
    }

    fun contieneNumeros(cadena: String): Boolean {
        return cadena.any { it.isDigit() }
    }

    fun contieneEspacios(cadena: String): Boolean {
        return cadena.none { it.isWhitespace() }
    }

    fun tieneMayuscula(cadena: String): Boolean {
        return cadena.any { it.isUpperCase() }
    }

    fun tieneMinuscula(cadena: String): Boolean {
        return cadena.any { it.isLowerCase() }
    }

    fun calcularSimilitud(cadena1:String, cadena2:String):Double {
        val cadena1Lower = cadena1.lowercase()
        val cadena2Lower = cadena2.lowercase()
        val maxLength = max(cadena1Lower.length, cadena2Lower.length)
        var similitud = 0
        for (i in 0 until cadena1Lower.length) {
            if (i >= cadena2Lower.length) break
            if (cadena1Lower[i] == cadena2Lower[i]) { similitud++ }
        }
        return similitud.toDouble() / maxLength
    }

    fun validarNombre(nombre:String):String {
        if (nombre.length < 10 || nombre.length > 30) {
            return "Por favor, ingrese un nombre válido con longitud de 10 a 30 caracteres"
        } else if (contieneNumeros(nombre)) {
            return "Por favor, evite incluir números en el nombre"
        }
        return ""
    }

    fun validarPassword(password:String, confirmarPassword:String):String {
        if (password.length < 8) {
            return "Por favor, ingresa una contraseña con un mínimo de 8 caracteres"
        } else if (!contieneEspacios(password)) {
            return "Por favor, asegúrate que tu contraseña no contenga espacios"
        } else if (!tieneMayuscula(password)) {
            return "Por favor, asegúrate de incluir al menos una letra mayúscula en tu contraseña"
        } else if (!tieneMinuscula(password)) {
            return "Por favor, asegúrate de incluir al menos una letra minúscula en tu contraseña"
        } else if (!contieneNumeros(password)) {
            return "Por favor, asegúrate de incluir al menos un número en tu contraseña"
        } else if (calcularSimilitud(nombre, password) > 0.5 && calcularSimilitud(correo, password) > 0.5) {
            return "Por favor, elige una contraseña que no contenga información personal"
        }

        if (password.isNotEmpty() && password != confirmarPassword) {
            return "Las contraseñas no son iguales. Por favor, asegúrate de ingresarlas correctamente"
        }
        return ""
    }
}